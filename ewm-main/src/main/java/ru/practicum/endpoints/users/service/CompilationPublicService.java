package ru.practicum.endpoints.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.compilation.dto.CompilationMapper;
import ru.practicum.model.compilation.dto.CompilationResponseDTO;
import ru.practicum.model.compilation.repository.CompilationStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationPublicService {

    @Autowired
    private final CompilationStorage compilationStorage;

    @Autowired
    private final CompilationMapper compilationMapper;

    public CompilationResponseDTO getCompilation(Long id) {
        return compilationMapper.toDTO(
                compilationStorage.getCompilation(id)
        );
    }

    public List<CompilationResponseDTO> getCompilations(Boolean pinned, int from, int size) {
        if (pinned == null) {
            return compilationStorage.getCompilations(from, size)
                    .stream().map(compilationMapper::toDTO).collect(Collectors.toList());
        }
        return compilationStorage.getCompilationsByPinned(pinned, from, size)
                .stream().map(compilationMapper::toDTO).collect(Collectors.toList());
    }
}
