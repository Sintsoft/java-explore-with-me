package ru.practicum.endpoints.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.users.service.ParticipationPrivateService;
import ru.practicum.model.paticipation.dto.ParicipationResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class ParticipationPrivateController {

    @Autowired
    private final ParticipationPrivateService service;

    @Autowired
    private final StatisticClient statisticClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParicipationResponseDTO postRequest(@Positive @PathVariable Long userId,
                                               @Positive @RequestParam Long eventId) {
        return service.addRequest(userId, eventId);
    }

    @GetMapping
    public List<ParicipationResponseDTO> getRequests(@PathVariable Long userId,
                                                     HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getEvents. Entered");
        hitStatistic(request);
        return service.getParticipations(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public ParicipationResponseDTO canelRequest(@PathVariable Long userId,
                                                @PathVariable Long requestId) {
        return service.canelRequest(userId, requestId);
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
