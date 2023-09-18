package ru.practicum.endpoints.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.admin.service.EventAdminService;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventPatchRequestDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    @Autowired
    private final EventAdminService service;

    @Autowired
    private final StatisticClient statisticClient;


    @PatchMapping("/{eventId}")
    public EventFullResponseDTO patchEvent(@Valid @RequestBody EventPatchRequestDTO dto,
                                           @PathVariable Long eventId) {
        log.debug("LEVEL - Controller. METHOD - patchEvent. Entered");
        return service.updateEvent(dto, eventId);
    }

    @GetMapping
    public List<EventFullResponseDTO> getEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size,
                                                HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvents. Entered");
        hitStatistic(request);
        return service.getEvents(users, categories, states, rangeStart, rangeEnd, from, size);
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
