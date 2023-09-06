package ru.practicum.utility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private String status;

    private String reason;

    private String message;

    private LocalDateTime timestamp;

}
