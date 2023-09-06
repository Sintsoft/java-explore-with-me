package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatUriHitsResponseDTO;
import ru.practicum.model.StatEntry;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatStorage {

    @Autowired
    private final StatRepository repository;

    @Autowired
    private final NamedParameterJdbcTemplate jdbc;

    @Autowired
    private final SessionFactory sessionFactory;

    @Transactional
    public void saveHit(StatEntry statisticEntry) {
        try {
            repository.save(statisticEntry);
        } catch (DataIntegrityViolationException exception) {
            String message = "Failed to save statistic entry due to: " + exception.getMessage();
            log.warn(message);
            throw new EwmSQLFailedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<StatUriHitsResponseDTO> getStatsUnique(LocalDateTime start,
                                                       LocalDateTime end,
                                                       boolean unique,
                                                       List<String> uris) {
        try (Session session = sessionFactory.openSession();) {
            String sql = "select s.application as \"app\", s.uri as \"uri\", count(*) as \"hits\" from " +
                    "(select " + (unique ? "distinct " : "") + "application, uri " +
                    "from stat_log where hit_timestamp between :start and :end and " +
                    "uri in (:uris)) s group by application, uri";
            List<Object[]> dtos = session.createNativeQuery(sql)
                    .setParameter("start", start).setParameter("end", end).setParameter("uris", uris).list();
            return dtos.stream()
                    .map(object -> new StatUriHitsResponseDTO(
                            (String) object[0],
                            (String) object[1],
                            (BigInteger) object[2])).collect(Collectors.toList());
        } catch (SQLGrammarException ex) {
            log.warn("getStatsUnique - " + ex.getMessage() + "\n" + ex.getStackTrace().toString());
            throw new EwmSQLFailedException("Failed to get statistic due to: " + ex.getMessage());
        }

    }

    @Transactional(readOnly = true)
    public List<StatUriHitsResponseDTO> getStats(LocalDateTime start,
                                                 LocalDateTime end,
                                                 Boolean unique,
                                                 List<String> uris) {
        log.trace("Enter method StatStorage.getStats");
        log.debug("Getting statistic params: start - " + start.toString()
                + ", end - " + end.toString() + ", unique - " + unique + ", uris - " + uris);
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("uris", uris)
                .addValue("start", start)
                .addValue("end", end);
        List<StatUriHitsResponseDTO> dtos = jdbc.queryForStream(
                formatSQLForHitStatistic(uris != null && !uris.isEmpty(), unique),
                namedParameters,
                new StatisticMapper()).collect(Collectors.toList());
        return dtos;
    }

    private String formatSQLForHitStatistic(boolean listIsEmpty, boolean unique) {
        StringBuilder sql = new StringBuilder().append("select uri, application, count(*) as cnt from ( select ");
        if (unique) {
            sql.append("distinct ");
        }
        sql.append("uri, application, ip from stat_log sl where ");
        if (listIsEmpty) {
            log.info("Not empty uris in request");
            sql.append("uri in (:uris) and ");
        }
        sql.append("hit_timestamp BETWEEN :start and :end ) sub group by uri, application order by cnt desc");
        return sql.toString();

    }

    private static class StatisticMapper implements RowMapper<StatUriHitsResponseDTO> {

        @Override
        public StatUriHitsResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StatUriHitsResponseDTO(
                    rs.getString("uri"),
                    rs.getString("application"),
                    BigInteger.valueOf((long) rs.getObject("cnt")));
        }
    }
}
