package ru.practicum.model.paticipation.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventPartisipationDecisionDTO {

    List<Long> requestIds;

    String status;
}
