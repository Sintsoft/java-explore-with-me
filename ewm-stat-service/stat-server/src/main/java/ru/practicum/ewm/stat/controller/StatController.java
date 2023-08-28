package ru.practicum.ewm.stat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stat.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService service;

    @GetMapping("/stats")
    public void getStat(@RequestParam String start,
                        @RequestParam String end,
                        @RequestParam(defaultValue = "false") Boolean unique,
                        @RequestParam List<String> uri) {
        service.getStats(null, null, false, List.of());
    }

    private LocalDateTime decodeTime(String time) {
        return null;
    }
}
