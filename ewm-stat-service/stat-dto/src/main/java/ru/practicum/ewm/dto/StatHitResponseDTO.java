package ru.practicum.ewm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatHitResponseDTO {

    private Long id;

    private String uri;

    private String app;

    private String ip;

    private LocalDateTime timestamp;
}
