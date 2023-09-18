package ru.practicum.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatUriHitsResponseDTO;
import ru.practicum.model.StatEntry;
import ru.practicum.utility.exceptions.EwmSQLFailedException;

import java.math.BigInteger;
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
                    "( select " + (unique ? "distinct " : "") + "application, uri, ip " +
                    "from stat_log where hit_timestamp between :start and :end " +
                    (uris != null && !uris.isEmpty() ? "and uri in (:uris) " : "") + ") s group by application, uri" +
                    " order by \"hits\" desc";
            NativeQuery nativeQuery = session.createNativeQuery(sql)
                    .setParameter("start", start).setParameter("end", end);
            if (uris != null && !uris.isEmpty()) {
                nativeQuery.setParameter("uris", uris);
            }
            List<Object[]> response = nativeQuery.list();
            return response.stream()
                    .map(object -> new StatUriHitsResponseDTO(
                            (String) object[0],
                            (String) object[1],
                            (BigInteger) object[2])).collect(Collectors.toList());
        } catch (SQLGrammarException ex) {
            log.warn("getStatsUnique - " + ex.getMessage() + "\n" + ex.getStackTrace().toString());
            throw new EwmSQLFailedException("Failed to get statistic due to: " + ex.getMessage());
        }

    }

}
