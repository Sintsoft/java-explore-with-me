package ru.practicum.model.compilation.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CompilationPostRequestDTO {

    private List<Long> events;

    private Boolean pinned = false;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;
}
