package ru.practicum.model.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.model.category.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category fromDTO(CategoryRequestDTO dto);

    CategoryResponseDTO toDTO(Category category);
}
