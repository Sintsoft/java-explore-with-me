package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class EventPatchRequestDTO {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 7000)
    private String description;

    private Boolean paid;

    private Boolean requestModeration;

    private Integer participantLimit;

    private LocationDTO location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private String stateAction;
}
