package ru.practicum.endpoints.parents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatUriHitsResponseDTO;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.event.dto.EventShortResponseDTO;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.model.paticipation.Participation;
import ru.practicum.model.paticipation.repository.ParticipationStorage;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    @Autowired
    protected final EventStorage eventStorage;

    @Autowired
    protected final UserStorage userStorage;

    @Autowired
    protected final CategoryStorage categoryStorage;

    @Autowired
    protected final ParticipationStorage participationStorage;

    @Autowired
    protected final EventMapper eventMapper;

    @Autowired
    protected final StatisticClient statisticClient;

    protected void startBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new EwmInvalidRequestParameterException("Start shoul be before end");
        }
    }

    protected List<EventFullResponseDTO> combineFull(List<Event> events,
                                                     List<Participation> requests,
                                                     Map<Long, Integer> views) {
        Map<Long, Integer> reqMap = new HashMap<>();
        requests.stream().forEach(request -> {
            if (reqMap.containsKey(request.getEvent().getId())) {
                reqMap.replace(request.getEvent().getId(), reqMap.get(request.getEvent().getId()) + 1);
            } else {
                reqMap.put(request.getEvent().getId(), 1);
            }
        });
        return events.stream().map(event -> eventMapper.toFullDTO(
                event,
                reqMap.getOrDefault(event.getId(), 0),
                views.getOrDefault(event.getId(), 0))).collect(Collectors.toList());
    }

    protected List<EventShortResponseDTO> combineShort(List<Event> events,
                                                       List<Participation> requests,
                                                       Map<Long, Integer> views) {
        Map<Long, Integer> reqMap = new HashMap<>();
        if (events.isEmpty()) return List.of();
        requests.stream().forEach(request -> {
            if (reqMap.containsKey(request.getEvent().getId())) {
                reqMap.replace(request.getEvent().getId(), reqMap.get(request.getEvent().getId()) + 1);
            } else {
                reqMap.put(request.getEvent().getId(), 1);
            }
        });
        return events.stream().map(event -> eventMapper.toShortDTO(
                event,
                reqMap.getOrDefault(event.getId(), 0),
                views.getOrDefault(event.getId(), 0))).collect(Collectors.toList());
    }

    protected Map<Long, Integer> getEventsStatistics(List<Event> events) {
        Map<Long, Integer> views = new HashMap<>();
        if (events.isEmpty()) return Map.of();
        List<StatUriHitsResponseDTO> dtos = statisticClient.getStatistic(
                        LocalDateTime.now().minusYears(100),
                        LocalDateTime.now().plusYears(100),
                        true,
                        events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList()));
        dtos.stream().forEach(entity -> {
                    log.info("EVENTID=" + entity.getUri().substring(8));
                    views.put(Long.parseLong(entity.getUri().substring(8)), entity.getHits().intValue());
                });
        return views;
    }
}
