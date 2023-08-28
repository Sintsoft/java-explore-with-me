package ru.practicum.ewm.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UriStatisticResponseDTO {

    public String app;

    public String uri;

    public Integer hits;
}
