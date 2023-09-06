package ru.practicum.models.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NestedUserDTO {

    private Long id;

    private String name;
}
