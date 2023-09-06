package ru.practicum.endpoints.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.category.dto.CategoryRequestDTO;
import ru.practicum.model.category.dto.CategoryResponseDTO;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminService {

    @Autowired
    private final CategoryStorage categoryStorage;

    @Autowired
    private final CategoryMapper categoryMapper;

    public CategoryResponseDTO addCategory(CategoryRequestDTO dto) {
        log.debug("LEVEL - Service. METHOD - addCategory. Entered");
        return categoryMapper.toDTO(categoryStorage.saveCategory(categoryMapper.fromDTO(dto)));
    }

    public CategoryResponseDTO updateCategory(CategoryRequestDTO dto, Long catId) {
        log.debug("LEVEL - Service. METHOD - updateCategory. Entered");
        if (catId == null || catId < 1) {
            log.info("Incorrect user id. It must be > 0");
            throw new EwmInvalidRequestParameterException("Incorrect category id. It must be > 0");
        }
        log.debug("LEVEL - Service. METHOD - updateCategory. Category found");
        Category category = categoryStorage.getCategory(catId);
        category.setName(dto.getName());
        return categoryMapper.toDTO(categoryStorage.saveCategory(category));
    }

    public void deleteCategory(Long catId) {
        log.debug("LEVEL - Service. METHOD - deleteCategory. Entered");
        if (catId == null || catId < 1) {
            log.info("Incorrect category id. It must be > 0");
            throw new EwmInvalidRequestParameterException("Incorrect category id. It must be > 0");
        }
        categoryStorage.removeCategory(catId);
    }
}
