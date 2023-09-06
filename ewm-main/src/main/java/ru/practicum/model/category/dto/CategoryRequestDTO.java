package ru.practicum.model.category.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CategoryRequestDTO {

    @NotBlank
    @NotNull
    @Size(max = 50)
    private String name;
}
