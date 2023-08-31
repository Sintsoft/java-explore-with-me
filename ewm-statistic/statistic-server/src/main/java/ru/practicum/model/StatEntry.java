package ru.practicum.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

    @NotNull
    @Column(name = "uri", nullable = false)
    private final String uri;

    @NotNull
    @Column(name = "application", nullable = false)
    private final String app;

    @NotNull
    @Column(name = "ip", nullable = false)
    private final String ip;

    @NotNull
    @Column(name = "hit_timestamp", nullable = false)
    private final LocalDateTime timestamp;
}
