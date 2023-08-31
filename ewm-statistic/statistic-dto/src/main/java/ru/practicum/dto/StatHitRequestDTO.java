package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatHitRequestDTO {

    private String uri;

    private String app;

    private String ip;

    private LocalDateTime timestamp;
}
