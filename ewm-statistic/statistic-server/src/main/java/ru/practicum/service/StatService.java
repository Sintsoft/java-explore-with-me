package ru.practicum.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.dto.StatUriHitsResponseDTO;
import ru.practicum.model.StatEntry;
import ru.practicum.repository.StatStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatService {

    @Autowired
    private final StatStorage storage;

    public void saveUrlHit(StatHitRequestDTO dto) {
        StatEntry statisticEntry = StatEntry.fromDTO(dto);

        storage.saveHit(statisticEntry);
    }

    public List<StatUriHitsResponseDTO> getStats(String start,
                                                 String end,
                                                 boolean unique,
                                                 List<String> uris) {
        log.info("Service. Method getStat. Params: start - " + start
                + ", end - " + end + ", unique - " + unique + ", uris - " + uris);
        LocalDateTime decStart = decodeTime(start);
        LocalDateTime decEnd  = decodeTime(end);
        if (decStart.isAfter(decEnd)) {
            log.info("Request start time is after end time");
            throw new EwmInvalidRequestParameterException("Start time should be earlier than end time");
        }
        return storage.getStatsUnique(decStart, decEnd, unique, uris);
    }

    private LocalDateTime decodeTime(String time) {
        log.info("Decoding time - " + time);
        try {
            if (time.matches("\\d*")) {
                log.info("Time of digits");
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis(Long.parseLong(time))), ZoneOffset.UTC);
            }
            return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception ex) {
            log.warn("Time string " + time + " can't be parsed. Cause - " + ex.getMessage());
            throw new EwmInvalidRequestParameterException("Cant parse time");
        }
    }

    private Long millis(long timestamp) {
        if (timestamp >= 1E16 || timestamp <= -1E16) {
            return timestamp / 1_000_000;
        }
        if (timestamp >= 1E14 || timestamp <= -1E14) {
            return timestamp / 1_000;
        }
        if (timestamp >= 1E11 || timestamp <= -3E10) {
            return timestamp;
        }
        return timestamp * 1_000;
    }
}
