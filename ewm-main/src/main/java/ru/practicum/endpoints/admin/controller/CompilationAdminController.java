package ru.practicum.endpoints.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.endpoints.admin.service.CompilatonAdminService;
import ru.practicum.model.compilation.dto.CompilationPatchRequestDTO;
import ru.practicum.model.compilation.dto.CompilationPostRequestDTO;
import ru.practicum.model.compilation.dto.CompilationResponseDTO;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {

    @Autowired
    private final CompilatonAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDTO postCompilation(@Valid @RequestBody CompilationPostRequestDTO dto) {
        log.debug("LEVEL - Controller. METHOD - postCompilation. Entered");
        return service.addCompilation(dto);
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDTO patchCompilation(@Valid @RequestBody CompilationPatchRequestDTO dto,
                                                   @PathVariable Long compId) {
        log.debug("LEVEL - Controller. METHOD - patchCompilation. Entered");
        return service.updateCompilation(dto, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.debug("LEVEL - Controller. METHOD - deleteCompilation. Entered");
        service.deleteCompilation(compId);
    }
}
