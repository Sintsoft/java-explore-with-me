package ru.practicum.endpoints.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.compilation.dto.CompilationMapper;
import ru.practicum.model.compilation.dto.CompilationPatchRequestDTO;
import ru.practicum.model.compilation.dto.CompilationPostRequestDTO;
import ru.practicum.model.compilation.dto.CompilationResponseDTO;
import ru.practicum.model.compilation.repository.CompilationStorage;
import ru.practicum.model.event.repository.EventStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilatonAdminService {

    @Autowired
    private final CompilationStorage compilationStorage;

    @Autowired
    private final EventStorage eventStorage;

    @Autowired
    private final CompilationMapper compilationMapper;

    public CompilationResponseDTO addCompilation(CompilationPostRequestDTO dto) {
        log.debug("LEVEL - Service. METHOD - addCompilation. Entered");
        return compilationMapper.toDTO(
                compilationStorage.saveCompilation(
                        compilationMapper.fromDTO(
                                dto,
                                eventStorage.getEventsByListId(dto.getEvents())
                        )
                ));
    }

    public CompilationResponseDTO updateCompilation(CompilationPatchRequestDTO dto, Long compId) {
        log.debug("LEVEL - Service. METHOD - updateCompilation. Entered");
        if (compId == null || compId < 1) {
            throw new EwmInvalidRequestParameterException("Id munst be positive number");
        }
        return compilationMapper.toDTO(
                compilationStorage.saveCompilation(
                        compilationMapper.fromDTO(
                                compilationStorage.getCompilation(compId),
                                dto,
                                eventStorage.getEventsByListId(dto.getEvents())
                        )
                ));
    }

    public void deleteCompilation(Long compId) {
        log.debug("LEVEL - Service. METHOD - deleteCompilation. Entered");
        if (compId == null || compId < 1) {
            throw new EwmInvalidRequestParameterException("Id munst be positive number");
        }
        compilationStorage.removeCompilation(compId);
    }

}