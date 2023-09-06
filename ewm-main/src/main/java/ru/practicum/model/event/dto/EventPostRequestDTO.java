package ru.practicum.model.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class EventPostRequestDTO {

    @NotBlank
    @NotNull
    @Size(min = 20, message = "{validation.name.size.too_short}")
    @Size(max = 2000, message = "{validation.name.size.too_long}")
    private String annotation;

    @NotNull
    private Long category;

    @NotNull
    @NotBlank
    @Size(min = 3, message = "{validation.name.size.too_short}")
    @Size(max = 120, message = "{validation.name.size.too_long}")
    private String title;

    @NotNull
    @NotBlank
    @Size(min = 20, message = "{validation.name.size.too_short}")
    @Size(max = 7000, message = "{validation.name.size.too_long}")
    private String description;

    private Boolean paid = false;

    private Boolean requestModeration = true;

    private Integer participantLimit = 0;

    @NotNull
    private LocationDTO location;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
}
