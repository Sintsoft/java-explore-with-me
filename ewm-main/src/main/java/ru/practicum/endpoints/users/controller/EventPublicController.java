package ru.practicum.endpoints.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.users.service.EventPublicService;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventShortResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    @Autowired
    private final EventPublicService service;

    @Autowired
    private final StatisticClient statisticClient;

    @GetMapping("/{eventId}")
    public EventFullResponseDTO getEvent(@PathVariable Long eventId,
                                         HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvent. Entered");
        hitStatistic(request);
        return service.getEvent(eventId);
    }

    @GetMapping
    public List<EventShortResponseDTO> getEvents(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvents. Entered");
        hitStatistic(request);
        return service.getEvents(text, categories, paid, onlyAvailable, sort, rangeStart, rangeEnd, from, size);
    }

    private void hitStatistic(HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - hitStatistic. Entered");
        StatHitRequestDTO dto = new StatHitRequestDTO();
        dto.setApp("ewm-main-service");
        dto.setIp(request.getRemoteAddr());
        dto.setUri(request.getRequestURI());
        dto.setTimestamp(LocalDateTime.now());
        statisticClient.hitUri(dto);
        log.debug("LEVEL - Controller. METHOD - hitStatistic. Exiting");
    }

}
