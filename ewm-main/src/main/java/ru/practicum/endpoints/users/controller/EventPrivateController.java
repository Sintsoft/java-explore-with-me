package ru.practicum.endpoints.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.endpoints.users.service.EventPrivateService;
import ru.practicum.model.event.dto.EventFullResponseDTO;
import ru.practicum.model.event.dto.EventPatchRequestDTO;
import ru.practicum.model.event.dto.EventPostRequestDTO;
import ru.practicum.model.event.dto.EventShortResponseDTO;
import ru.practicum.model.paticipation.dto.EventPartisipationDecisionDTO;
import ru.practicum.model.paticipation.dto.ParicipationResponseDTO;
import ru.practicum.model.paticipation.dto.ParticipationDecisionResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {

    @Autowired
    private final EventPrivateService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullResponseDTO postEvent(@Valid @RequestBody EventPostRequestDTO dto,
                                          @PathVariable Long userId) {
        log.debug("LEVEL - Controller. METHOD - postEvent. Entered");
        return service.addEvent(dto, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullResponseDTO patchEvent(@Valid @RequestBody EventPatchRequestDTO dto,
                                           @PathVariable Long userId,
                                           @PathVariable Long eventId) {
        log.debug("LEVEL - Controller. METHOD - patchEvent. Entered");
        return service.updateEvent(dto, userId, eventId);
    }

    @GetMapping
    public List<EventShortResponseDTO> getEvents(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvents. Entered");
        return service.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullResponseDTO getEvent(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvent. Entered");
        return service.getEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParicipationResponseDTO> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId,
                                                     HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getRequests. Entered");
        return service.getEvetRequests(eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public ParticipationDecisionResponseDTO patchRequests(@RequestBody EventPartisipationDecisionDTO dto,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long eventId,
                                                          HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - patchRequests. Entered");
        return service.patchEventRequests(dto, userId, eventId);
    }

}
