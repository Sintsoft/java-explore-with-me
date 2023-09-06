package ru.practicum.models.user.dto;


import lombok.Data;

@Data
public class RequestUserDTO {

    Long id;

    String name;

    String email;
}
