package ru.practicum.ewm.stat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.StatHitRequestDTO;
import ru.practicum.ewm.dto.UriStatisticResponseDTO;
import ru.practicum.ewm.stat.service.StatService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void postHit(@RequestBody StatHitRequestDTO dto) {
        service.saveUrlHit(dto);
    }

    @GetMapping("/stats")
    public List<UriStatisticResponseDTO> getStat(@RequestParam String start,
                                                 @RequestParam String end,
                                                 @RequestParam(defaultValue = "false") Boolean unique,
                                                 @RequestParam(value = "uris", required = false) List<String> uris) {
        log.info("Controller. Method getStat. Params: start - " + start
            + ", end - " + end + ", unique - " + unique + ", uris - " + uris);
        return service.getStats(start, end, unique, uris);
    }


}
