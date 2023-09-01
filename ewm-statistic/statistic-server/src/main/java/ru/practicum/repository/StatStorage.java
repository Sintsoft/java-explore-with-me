package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        return jdbc.queryForStream(
                formatSQLForHitStatistic(uris != null && !uris.isEmpty(), unique),
                namedParameters,
                new StatisticMapper()).collect(Collectors.toList());
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
                    rs.getInt("cnt"));
        }
    }
}
