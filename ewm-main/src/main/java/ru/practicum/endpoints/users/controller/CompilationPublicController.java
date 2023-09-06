package ru.practicum.endpoints.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.users.service.CompilationPublicService;
import ru.practicum.model.compilation.dto.CompilationResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationPublicController {

    @Autowired
    private final CompilationPublicService service;

    @Autowired
    private final StatisticClient statisticClient;

    @GetMapping
    public List<CompilationResponseDTO> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                        @RequestParam(defaultValue = "0") int from,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getCompilations. Entered");
        hitStatistic(request);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationResponseDTO getCompilation(@PathVariable Long compId,
                                                 HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getCompilations. Entered");
        hitStatistic(request);
        return service.getCompilation(compId);
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
