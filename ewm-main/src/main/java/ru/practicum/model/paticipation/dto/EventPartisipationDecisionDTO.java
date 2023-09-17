package ru.practicum.model.paticipation.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventPartisipationDecisionDTO {

    private List<Long> requestIds;

    private String status;
}
