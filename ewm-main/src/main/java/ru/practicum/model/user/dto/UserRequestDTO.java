package ru.practicum.model.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequestDTO {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    @NotNull
    @Email
    @NotBlank
    @Size(min = 6, max = 254)
    private String email;
}
