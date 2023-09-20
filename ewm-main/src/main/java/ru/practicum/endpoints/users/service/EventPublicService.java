package ru.practicum.endpoints.users.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;
import ru.practicum.endpoints.parents.EventService;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.event.dto.EventShortResponseDTO;
import ru.practicum.model.event.enums.EventSorts;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.model.paticipation.enums.ParticipationRequestStatus;
import ru.practicum.model.paticipation.repository.ParticipationStorage;
import ru.practicum.model.user.User;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.commons.Constants;
import ru.practicum.utility.exceptions.EwmEntityNotFoundException;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EventPublicService extends EventService {

    public EventPublicService(EventStorage eventStorage,
                             UserStorage userStorage,
                             CategoryStorage categoryStorage,
                             ParticipationStorage participationStorage,
                             EventMapper eventMapper,
                             StatisticClient statisticClient) {
        super(
                eventStorage,
                userStorage,
                categoryStorage,
                participationStorage,
                eventMapper,
                statisticClient);
    }

    public EventFullResponseDTO getEvent(Long eventId) {
        Event event = eventStorage.getEvent(eventId);
        if (event.getState() != EventStates.PUBLISHED) {
            throw new EwmEntityNotFoundException("Event with id = " + eventId + " was not found");
        }
        Map<Long, Integer> views = getEventsStatistics(List.of(event));
        return eventMapper.toFullDTO(event,
                participationStorage.getEventsParticipations(List.of(event), true).size(),
                views.getOrDefault(eventId, 0));
    }

    public List<EventShortResponseDTO> getEvents(String text,
                                                 List<Long> categories,
                                                 Boolean paid,
                                                 Boolean onlyAvailable,
                                                 String sort,
                                                 String start,
                                                 String end,
                                                 int from,
                                                 int size

    ) {
        startBeforeEnd(parseRequestDateTime(start), parseRequestDateTime(end));
        List<Event> events = eventStorage.searchForEvents(
                        null,
                        categories == null ? null : categoryStorage.getCategoriesByListId(categories),
                        List.of(EventStates.PUBLISHED),
                        text,
                        paid,
                        onlyAvailable,
                        sort == null ? null : parseSort(sort),
                        start == null ? LocalDateTime.now() : parseRequestDateTime(start),
                        parseRequestDateTime(end),
                        from,
                        size,
                        false,
                        true);
        return combineShort(events,
                participationStorage.getEventsParticipations(events, true),
                getEventsStatistics(events));
    }

    public void likeEvent(Long eventId, Long userId) {
        Event event = eventStorage.getEvent(eventId);
        User user = userStorage.getUser(userId);
        if(user.equals(event.getInitiator())) {
            throw new EwmRequestParameterConflict("Initiator can't like event");
        }
        if (participationStorage.getUserParticipations(user)
                .stream().noneMatch(request -> request.getStatus().equals(ParticipationRequestStatus.CONFIRMED))) {
            throw new EwmInvalidRequestParameterException("User can't like event without confirmed request participation");
        }
        event.getLikers().add(user);
        eventStorage.saveEvent(event);

    }

    public void dislikeEvent(Long eventId, Long userId) {
        Event event = eventStorage.getEvent(eventId);
        User user = userStorage.getUser(userId);
        if (user.equals(event.getInitiator())) {
            throw new EwmRequestParameterConflict("Initiator can't like event");
        }
        if (!event.getLikers().contains(user)) {
            throw new EwmInvalidRequestParameterException("User can't dislike this event");
        }
        event.getLikers().remove(user);
        eventStorage.saveEvent(event);

    }

    private EventSorts parseSort(String sortString) {
        try {
            return EventSorts.valueOf(sortString);
        } catch (Exception ex) {
            throw new EwmInvalidRequestParameterException("Incorrect sort in request parameters");
        }
    }

    private LocalDateTime parseRequestDateTime(String input) {
        try {
            if (input == null) {
                return null;
            }
            return LocalDateTime.parse(input, Constants.DATE_TIME_FORMAT);
        } catch (Exception ex) {
            throw new EwmInvalidRequestParameterException("Incorrect time format in request parameters");
        }
    }
}
