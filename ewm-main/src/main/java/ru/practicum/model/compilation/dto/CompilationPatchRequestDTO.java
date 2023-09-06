package ru.practicum.model.compilation.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CompilationPatchRequestDTO {

    private List<Long> events;

    private Boolean pinned = false;

    @Size(max = 50)
    private String title;
}
