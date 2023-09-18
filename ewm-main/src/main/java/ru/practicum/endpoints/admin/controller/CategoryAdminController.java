package ru.practicum.endpoints.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.endpoints.admin.service.CategoryAdminService;
import ru.practicum.model.category.dto.CategoryRequestDTO;
import ru.practicum.model.category.dto.CategoryResponseDTO;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    @Autowired
    private final CategoryAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDTO postCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        log.debug("LEVEL - Controller. METHOD - postCategory. Entered");
        return service.addCategory(dto);
    }

    @PatchMapping("/{catId}")
    public CategoryResponseDTO patchCategory(@Valid @RequestBody CategoryRequestDTO dto,
                                             @PathVariable Long catId) {
        log.debug("LEVEL - Controller. METHOD - patchCategory. Entered");
        return service.updateCategory(dto, catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("LEVEL - Controller. METHOD - deleteCategory. Entered");
        service.deleteCategory(catId);
    }
}
