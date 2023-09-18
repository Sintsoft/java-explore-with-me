package ru.practicum.endpoints.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.category.dto.CategoryListMapper;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.category.dto.CategoryResponseDTO;
import ru.practicum.model.category.repository.CategoryStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.validation.CommonVaidationChecks;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryPublicService {

    @Autowired
    private final CategoryStorage categoryStorage;

    @Autowired
    private final CategoryMapper categoryMapper;

    @Autowired
    private final CategoryListMapper categoryListMapper;

    public CategoryResponseDTO getCategory(Long catId) {
        log.debug("LEVEL - Service. METHOD - getCategory. Entered");
        if (catId == null || catId < 1) {
            log.info("Incorrect user id. It must be > 0");
            throw new EwmInvalidRequestParameterException("Incorrect category id. It must be > 0");
        }
        return categoryMapper.toDTO(categoryStorage.getCategory(catId));
    }

    public List<CategoryResponseDTO> getCategories(int from, int size) {
        log.debug("LEVEL - Service. METHOD - getCategory. Entered");
        CommonVaidationChecks.paginationParamsValidation(from,size);
        return categoryListMapper.toDTOList(categoryStorage.getCategories(from,size));
    }
}
