package ru.practicum.ewm.stat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Data
@AllArgsConstructor
public class StatHitRequestDTO {

    private String uri;

    private String app;

    private String ip;

    private LocalDateTime timestamp;
}
