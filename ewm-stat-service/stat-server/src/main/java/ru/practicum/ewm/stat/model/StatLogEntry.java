package ru.practicum.ewm.stat.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.ewm.stat.dto.StatHitRequestDTO;
import ru.practicum.ewm.stat.dto.StatHitResponseDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Slf4j
@Entity
@Table(name = "stat_log")
public class StatLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "uri", nullable = false)
    private final String uri;

    @Column(name = "application", nullable = false)
    private final String app;

    @Column(name = "ip", nullable = false)
    private final String ip;

    @Column(name = "hit_timestamp", nullable = false)
    private final LocalDateTime timestamp;

    public static StatLogEntry fromDTO(StatHitRequestDTO dto) {
        log.debug("Creating entry from DTO. dto - " + dto.toString());
        return new StatLogEntry(
                null,
                dto.getUri(),
                dto.getApp(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }

}
