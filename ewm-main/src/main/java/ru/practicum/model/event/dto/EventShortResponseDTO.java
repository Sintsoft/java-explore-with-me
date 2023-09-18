package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.model.category.dto.CategoryResponseDTO;
import ru.practicum.model.user.dto.UserNestedDTO;

import java.time.LocalDateTime;

@Data
public class EventShortResponseDTO {

    private String annotation;

    private CategoryResponseDTO category;

    private Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    private UserNestedDTO initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
