package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatUriHitsResponseDTO {

    private String app;

    private String uri;

    private BigInteger hits;
}
