package ru.practicum.model.paticipation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParticipationDecisionResponseDTO {

    private List<ParicipationResponseDTO> confirmedRequests;

    private List<ParicipationResponseDTO> rejectedRequests;
}
