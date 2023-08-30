package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatUriHitsResponseDTO {

    private String app;

    private String uri;

    private Integer hits;
}
