package ru.practicum.ewm.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stat.dto.StatHitRequestDTO;
import ru.practicum.ewm.stat.dto.UriStatisticResponseDTO;
import ru.practicum.ewm.stat.model.StatLogEntry;
import ru.practicum.ewm.stat.repo.StatStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatService {

    @Autowired
    private final StatStorage storage;

    public void hitRes(StatHitRequestDTO dto) {
        storage.saveStatHit(StatLogEntry.fromDTO(dto));
    }

    public List<UriStatisticResponseDTO> getStats(LocalDateTime start,
                                                  LocalDateTime end,
                                                  Boolean unique,
                                                  List<String> uri) {

        return storage.getStats(start, end, unique, uri);
    }

}
