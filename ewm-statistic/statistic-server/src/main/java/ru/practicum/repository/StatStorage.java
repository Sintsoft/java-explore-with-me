package ru.practicum.repository;

import ru.practicum.dto.StatUriHitsResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.model.StatEntry;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import javax.transaction.Transactional;
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
    private final JdbcTemplate jdbc;

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

    public List<StatUriHitsResponseDTO> getStats(LocalDateTime start,
                                                 LocalDateTime end,
                                                 Boolean unique,
                                                 List<String> uris) {
        log.trace("Enter method StatStorage.getStats");
        log.debug("Getting statistic params: start - " + start.toString()
                + ", end - " + end.toString() + ", unique - " + unique + ", uris - " + uris);
        StringBuilder sql = new StringBuilder().append("select uri, application, count(*) as cnt from ( select ");
        if (unique) {
            sql.append("distinct ");
        }
        sql.append("uri, application, ip from stat_log sl where ");
        if (uris != null && !uris.isEmpty()) {
            log.info("Not empty uris in request");
            sql.append("uri in ('")
                    .append(String.join("', '", uris))
                    .append("') and ");
        }
        sql
            .append("'")
            .append(start.format(DateTimeFormatter.ISO_DATE_TIME))
            .append("' < hit_timestamp and '")
            .append(end.format(DateTimeFormatter.ISO_DATE_TIME))
            .append("' > hit_timestamp ) sub group by uri, application order by cnt desc");
        log.info("Formed query = " + sql);
        return jdbc.queryForStream(sql.toString(), new StatisticMapper()).collect(Collectors.toList());
    }

    private static class StatisticMapper implements RowMapper<StatUriHitsResponseDTO> {

        @Override
        public StatUriHitsResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new StatUriHitsResponseDTO(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3));
        }
    }
}
