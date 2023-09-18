package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.StatHitRequestDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Slf4j
@Entity
@Table(name = "stat_log")
@AllArgsConstructor
public class StatEntry {

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

    public static StatEntry fromDTO(StatHitRequestDTO dto) {
        log.debug("Creating entry from DTO. dto - " + dto.toString());
        return new StatEntry(
                null,
                dto.getUri(),
                dto.getApp(),
                dto.getIp(),
                dto.getTimestamp()
        );
    }
}
