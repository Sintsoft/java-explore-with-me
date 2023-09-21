package ru.practicum.endpoints.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;
import ru.practicum.endpoints.parents.EventService;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dao.EventViewEntity;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.event.enums.EventUserStateActions;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.model.paticipation.Participation;
import ru.practicum.model.paticipation.dto.EventPartisipationDecisionDTO;
import ru.practicum.model.paticipation.dto.ParicipationResponseDTO;
import ru.practicum.model.paticipation.dto.ParticipationDecisionResponseDTO;
import ru.practicum.model.paticipation.dto.PartisipationMapper;
import ru.practicum.model.paticipation.enums.ParticipationRequestStatus;
import ru.practicum.model.paticipation.repository.ParticipationStorage;
import ru.practicum.model.user.User;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPrivateService extends EventService {

    @Autowired
    private final PartisipationMapper partisipationMapper;

    public EventPrivateService(EventStorage eventStorage,
                               UserStorage userStorage,
                               CategoryStorage categoryStorage,
                               ParticipationStorage participationStorage,
                               EventMapper eventMapper,
                               StatisticClient statisticClient,
                               PartisipationMapper partisipationMapper) {
        super(
                eventStorage,
                userStorage,
                categoryStorage,
                participationStorage,
                eventMapper,
                statisticClient);
        this.partisipationMapper = partisipationMapper;
    }

    public EventFullResponseDTO addEvent(EventPostRequestDTO dto, Long userId) {
        if (dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EwmInvalidRequestParameterException("Event date is too early");
        }
        Event event = eventStorage.saveEvent(
                eventMapper.fromDTO(
                        dto,
                        categoryStorage.getCategory(dto.getCategory()),
                        userStorage.getUser(userId))
        );
        return eventMapper.toFullDTO(
                event,
                participationStorage.getEventsParticipations(List.of(event), true).size(),
                0);
    }

    public EventFullResponseDTO updateEvent(EventPatchRequestDTO dto, Long userId, Long eventId) {
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EwmInvalidRequestParameterException("Event date is too early");
        }
        Event oldEvent = eventStorage.getEvent(eventId);
        if (oldEvent.getState() == EventStates.PUBLISHED) {
            throw new EwmRequestParameterConflict("U can't update published event");
        }
        Event event = eventMapper.fromDTO(
                oldEvent,
                eventId,
                dto,
                oldEvent.getCategory(),
                userStorage.getUser(userId)
        );
        if (dto.getStateAction() != null && !dto.getStateAction().isBlank()) {
            switch (EventUserStateActions.valueOf(dto.getStateAction())) {
                case SEND_TO_REVIEW: {
                    event.setSended(true);
                    event.setState(EventStates.PENDING);
                    break;
                }
                case CANCEL_REVIEW:
                    event.setState(EventStates.CANCELED);
                    break;
            }
        }
        return eventMapper.toFullDTO(eventStorage.saveEvent(event),
                participationStorage.getEventsParticipations(List.of(event), true).size(),
                getEventsStatistics(List.of(event.getId())).getOrDefault(eventId, 0));

    }

    public EventFullResponseDTO getEvent(Long userId, Long eventId) {
        Event event = eventStorage.getEvent(eventId);
        if (!event.getInitiator().equals(userStorage.getUser(userId))) {
            throw new EwmEntityNotFoundException("Can't found event");
        }
        return eventMapper.toFullDTO(
                event,
                participationStorage.getEventsParticipations(List.of(event), true).size(),
                getEventsStatistics(List.of(event.getId())).getOrDefault(eventId, 0));
    }

    public List<EventShortResponseDTO> getEvents(Long userId, int from, int size) {
        List<EventViewEntity> events = eventStorage.search(
                List.of(userStorage.getUser(userId)),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                from,
                size,
                false);
        Map<Long, Integer> map = getEventsStatistics(events.stream()
                .map(EventViewEntity::getId).collect(Collectors.toList()));
        return events.stream()
                .map(ev -> eventMapper.toShortDTO(ev, map.getOrDefault(ev.getId(), 0)))
                .collect(Collectors.toList());
    }

    public List<ParicipationResponseDTO> getEvetRequests(Long eventId) {
        return participationStorage.getEventsParticipations(
                List.of(eventStorage.getEvent(eventId)), null
        ).stream().map(partisipationMapper::toDTO).collect(Collectors.toList());
    }

    public ParticipationDecisionResponseDTO patchEventRequests(EventPartisipationDecisionDTO dto,
                                                               Long userId,
                                                               Long eventId) {
        log.debug("LEVEL - Service. METHOD - patchEventRequests. Entered");
        User user = userStorage.getUser(userId);
        Event event = eventStorage.getEvent(eventId);
        List<Participation> requests = participationStorage.getPartisipationsList(dto.getRequestIds());
        requests.stream().filter(request -> request.getEvent().getInitiator().equals(user)
                                            && request.getEvent().equals(event)).collect(Collectors.toList());
        if (ParticipationRequestStatus.valueOf(dto.getStatus()).equals(ParticipationRequestStatus.REJECTED)) {
            requests.stream().forEach(request -> {
                if (request.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                    throw new EwmRequestParameterConflict("Already confirmed");
                }
            });
        }
        if (ParticipationRequestStatus.valueOf(dto.getStatus()).equals(ParticipationRequestStatus.CONFIRMED)
                && participationStorage.getEventsParticipations(
                        List.of(event), true).size() >= event.getParticipantLimit()) {
            throw new EwmRequestParameterConflict("Request limit reached");
        }
        requests.stream().map(request -> {
                    switch (ParticipationRequestStatus.valueOf(dto.getStatus())) {
                        case CONFIRMED: request.setStatus(ParticipationRequestStatus.CONFIRMED); break;
                        case REJECTED: request.setStatus(ParticipationRequestStatus.REJECTED); break;
                    }
                    return request;
                }).collect(Collectors.toList());
        participationStorage.saveParticipationsList(requests);
        return new ParticipationDecisionResponseDTO(
                participationStorage.getEventsParticipations(List.of(event), true)
                        .stream().map(partisipationMapper::toDTO).collect(Collectors.toList()),
                participationStorage.getEventsParticipations(List.of(event), false)
                        .stream().map(partisipationMapper::toDTO).collect(Collectors.toList())
        );
    }
}
