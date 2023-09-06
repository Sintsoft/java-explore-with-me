package ru.practicum.model.paticipation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserNestedDTO;

import java.time.LocalDateTime;

@Data
public class ParicipationResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private String status;
}
