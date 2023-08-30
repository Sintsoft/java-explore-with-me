package ru.practicum.utility.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {

    private final String message;

}
