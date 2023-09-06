package ru.practicum.utility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private final String status;

    private final String reason;

    private final String message;

    private final LocalDateTime timestamp;

}
