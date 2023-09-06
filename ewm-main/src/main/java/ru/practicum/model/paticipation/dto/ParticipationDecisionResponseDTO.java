package ru.practicum.model.paticipation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParticipationDecisionResponseDTO {

    List<ParicipationResponseDTO> confirmedRequests;

    List<ParicipationResponseDTO> rejectedRequests;
}
