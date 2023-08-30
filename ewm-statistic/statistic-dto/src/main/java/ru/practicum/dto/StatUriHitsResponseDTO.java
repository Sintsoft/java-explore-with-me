package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
@AllArgsConstructor
public class StatUriHitsResponseDTO {

    private String app;

    private String uri;

    private Integer hits;
}
