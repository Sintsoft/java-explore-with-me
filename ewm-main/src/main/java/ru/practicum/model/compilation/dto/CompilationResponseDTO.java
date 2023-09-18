package ru.practicum.model.compilation.dto;

import lombok.Data;
import ru.practicum.model.event.dto.EventShortResponseDTO;

import java.util.List;

@Data
public class CompilationResponseDTO {

    private List<EventShortResponseDTO> events;

    private Boolean pinned;

    private String title;

    private Long id;

}
