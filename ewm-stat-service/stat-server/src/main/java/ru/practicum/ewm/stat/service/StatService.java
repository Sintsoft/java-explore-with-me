package ru.practicum.ewm.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stat.dto.StatHitRequestDTO;
import ru.practicum.ewm.stat.dto.UriStatisticResponseDTO;
import ru.practicum.ewm.stat.model.StatLogEntry;
import ru.practicum.ewm.stat.repo.StatStorage;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    @Autowired
    private final JdbcTemplate jdbc;

    @Autowired
    private final StatStorage storage;

    public void hitRes(StatHitRequestDTO dto) {

    }

    public List<UriStatisticResponseDTO> getStats(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Boolean unique,
                                                  List<String> uri) {

        return storage.getStats(start, end, unique, uri);
    }

}
