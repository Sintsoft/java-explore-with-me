package ru.practicum.endpoints.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.model.paticipation.Participation;
import ru.practicum.model.paticipation.dto.ParicipationResponseDTO;
import ru.practicum.model.paticipation.dto.PartisipationMapper;
import ru.practicum.model.paticipation.enums.ParticipationRequestStatus;
import ru.practicum.model.paticipation.repository.ParticipationStorage;
import ru.practicum.model.user.User;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationPrivateService {

    @Autowired
    public final PartisipationMapper partisipationMapper;

    @Autowired
    public final ParticipationStorage participationStorage;

    @Autowired
    private final EventStorage eventStorage;

    @Autowired
    private final UserStorage userStorage;

    public ParicipationResponseDTO addRequest(Long userId, Long evntId) {
        User user = userStorage.getUser(userId);
        Event event = eventStorage.getEvent(evntId);
        List<Participation> participations = participationStorage.getEventsParticipations(List.of(event), null);
        if (participations.stream()
                .anyMatch(participation -> participation.getRequester().getId().equals(userId))) {
            throw new EwmRequestParameterConflict("Already requeted participation");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new EwmRequestParameterConflict("Initiator can't send request");
        }
        if (event.getState() != EventStates.PUBLISHED) {
            throw new EwmRequestParameterConflict("Event not found");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit()
                <= participationStorage.getEventsParticipations(List.of(event), true).size()) {
            throw new EwmRequestParameterConflict("All slots are taken");
        }

        Participation participation = new Participation(event, user);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(ParticipationRequestStatus.CONFIRMED);
        }
        return partisipationMapper.toDTO(
                participationStorage.saveParticipation(participation));
    }

    public ParicipationResponseDTO canelRequest(Long userId, Long reqId) {
        User user = userStorage.getUser(userId);
        Participation participation = participationStorage.getParticipation(reqId);
        if (participation.getStatus().equals(ParticipationRequestStatus.REJECTED)) {
            throw new EwmRequestParameterConflict("Already rejected");
        }
        if (!participation.getRequester().equals(user)) {
            throw new EwmEntityNotFoundException("This user didn't made this request");
        }
        if (participation.getStatus() == ParticipationRequestStatus.CONFIRMED) {
            throw new EwmRequestParameterConflict("Can't cancel confirmed request");
        }
        participation.setStatus(ParticipationRequestStatus.CANCELED);
        return partisipationMapper.toDTO(participationStorage.saveParticipation(participation));
    }

    public List<ParicipationResponseDTO> getParticipations(Long userId) {
        return participationStorage.getUserParticipations(
                userStorage.getUser(userId))
                .stream().map(partisipationMapper::toDTO).collect(Collectors.toList());
    }
}
