package ru.practicum.model.paticipation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.paticipation.Participation;

@Mapper(componentModel = "spring")
public interface PartisipationMapper {

    @Mapping(source = "participation.event.id", target = "event")
    @Mapping(source = "participation.requester.id", target = "requester")
    ParicipationResponseDTO toDTO(Participation participation);
}
