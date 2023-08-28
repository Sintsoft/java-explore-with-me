package ru.practicum.ewm.stat.repo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stat.dto.UriStatisticResponseDTO;
import ru.practicum.ewm.stat.model.StatLogEntry;
import ru.practicum.ewm.stat.service.StatService;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatStorage {

    @Autowired
    private final StatEntryRepository repository;

    @Autowired
    private final JdbcTemplate jdbc;

    @Transactional
    public void saveStatHit(StatLogEntry entry) {}

    public List<UriStatisticResponseDTO> getStats(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Boolean unique,
                                                  List<String> uri) {
        StringBuilder sql = new StringBuilder().append("select uri, application, count(*) ");
        return jdbc.queryForStream("select uri, application, count(*) " +
                "from stat_log sl " + "group by uri, application", new StatisticMapper()).collect(Collectors.toList());
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
