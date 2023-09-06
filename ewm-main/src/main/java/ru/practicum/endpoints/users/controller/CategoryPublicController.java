package ru.practicum.endpoints.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.users.service.CategoryPublicService;
import ru.practicum.model.category.dto.CategoryResponseDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {

    @Autowired
    private final CategoryPublicService service;

    @Autowired
    private final StatisticClient statisticClient;

    @GetMapping("/{catId}")
    public CategoryResponseDTO getCategory(@PathVariable Long catId,
                                           HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getCategory. Entered");
        hitStatistic(request);
        return service.getCategory(catId);
    }

    @GetMapping
    public List<CategoryResponseDTO> getCategory(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getCategory. Entered");
        hitStatistic(request);
        return service.getCategories(from, size);
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
