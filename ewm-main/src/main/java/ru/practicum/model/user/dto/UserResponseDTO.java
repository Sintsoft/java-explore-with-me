package ru.practicum.model.user.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;
}
