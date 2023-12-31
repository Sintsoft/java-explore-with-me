package ru.practicum.endpoints.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;
import ru.practicum.endpoints.parents.EventService;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dao.EventViewEntity;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.event.dto.EventPatchRequestDTO;
import ru.practicum.model.event.enums.EventAdminStateActions;
import ru.practicum.model.event.enums.EventStates;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.model.paticipation.repository.ParticipationStorage;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.commons.Constants;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.exceptions.EwmRequestParameterConflict;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventAdminService extends EventService {

    public EventAdminService(EventStorage eventStorage,
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

    public EventFullResponseDTO updateEvent(EventPatchRequestDTO dto, Long eventId) {
        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EwmInvalidRequestParameterException("Event date is too early");
        }
        Event event = eventMapper.fromDTO(
            eventStorage.getEvent(eventId),
            eventId,
            dto,
            null,
            null);

        if (dto.getStateAction() != null && !dto.getStateAction().isBlank()) {
            switch (EventAdminStateActions.valueOf(dto.getStateAction())) {
                case REJECT_EVENT: {
                    if (event.getState() == EventStates.PUBLISHED) {
                        throw new EwmRequestParameterConflict("Can't cancel published event");
                    }
                    event.setState(EventStates.CANCELED);
                    break;
                }
                case PUBLISH_EVENT: {
                    if (event.getState() != EventStates.PENDING) {
                        throw new EwmRequestParameterConflict("Can't publish event");
                    }
                    event.setState(EventStates.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                }
            }
        }
        return eventMapper.toFullDTO(
                eventStorage.saveEvent(event),
                participationStorage.getEventsParticipations(List.of(event), true).size(),
                getEventsStatistics(List.of(event.getId())).getOrDefault(eventId, 0));
    }

    public List<EventFullResponseDTO> getEvents(List<Long> users,
                                                List<Long> categories,
                                                List<String> states,
                                                String start,
                                                String end,
                                                int from,
                                                int size) {
        startBeforeEnd(parseRequestDateTime(start), parseRequestDateTime(end));
        List<EventViewEntity> events = eventStorage.search(
                        users != null ? userStorage.getUsersByListId(users) : null,
                        categories != null ? categoryStorage.getCategoriesByListId(categories) : null,
                        states != null ? parseStates(states) : null,
                        null,
                        null,
                        null,
                        null,
                        parseRequestDateTime(start),
                        parseRequestDateTime(end),
                        from,
                        size,
                        false);
        Map<Long, Integer> map = getEventsStatistics(events.stream()
                .map(EventViewEntity::getId).collect(Collectors.toList()));
        return events.stream()
                .map(ev -> eventMapper.toFullDTO(ev, map.getOrDefault(ev.getId(), 0)))
                .collect(Collectors.toList());
    }

    private List<EventStates> parseStates(List<String> stateStrings) {
        try {
            if (stateStrings != null && !stateStrings.isEmpty()) {
                return stateStrings.stream().map(EventStates::valueOf).collect(Collectors.toList());
            }
            return List.of();
        } catch (Exception ex) {
            throw new EwmInvalidRequestParameterException("Incorrect state in request parameters");
        }
    }

    private LocalDateTime parseRequestDateTime(String input) {
        try {
            return input == null ? null : LocalDateTime.parse(input, Constants.DATE_TIME_FORMAT);
        } catch (Exception ex) {
            throw new EwmInvalidRequestParameterException("Incorrect time format in request parameters");
        }
    }
}
