package ru.practicum.ewm.stat.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.UriStatisticResponseDTO;
import ru.practicum.ewm.stat.model.StatLogEntry;
import ru.practicum.ewm.utility.exceptions.EwmSQLFailedException;

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
    private final StatEntryRepository repository;

    @Autowired
    private final JdbcTemplate jdbc;

    @Transactional
    public void saveStatHit(StatLogEntry entry) {
        log.trace("Enter method StatStorage.saveStatHit");
        log.debug("Saving entry: " + entry.toString());
        try {
            repository.save(entry);
        } catch (Exception ex) {
            throw new EwmSQLFailedException("Failed to add statistic entry due to: " + ex.getMessage());
        }
    }

    public List<UriStatisticResponseDTO> getStats(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Boolean unique,
                                                  List<String> uris) {
        log.trace("Enter method StatStorage.getStats");
        log.debug("Getting statistic params: start - " + start.toString()
                + ", end - " + end.toString() + ", unique - " + unique + ", uris - " + uris.toString());
        StringBuilder sql = new StringBuilder().append("select uri, application, count(*) from ( select ");
        if (unique) {
            sql.append("distinct ");
        }
        sql.append("uri, application, ip from stat_log sl where uri in (");
        for (String uri : uris) {
            sql.append("'" + uri + "'");
        }
        sql.append(") and '" + start.format(DateTimeFormatter.ISO_DATE_TIME) + "' < hit_timestamp and '"
                + end.format(DateTimeFormatter.ISO_DATE_TIME) + "' > hit_timestamp");
        sql.append(") sub group by uri, application");
        log.info("Formed query = " + sql);
        return jdbc.queryForStream(sql.toString(), new StatisticMapper()).collect(Collectors.toList());
    }

    private static class StatisticMapper implements RowMapper<UriStatisticResponseDTO> {

        @Override
        public UriStatisticResponseDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UriStatisticResponseDTO(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3));
        }
    }

}
