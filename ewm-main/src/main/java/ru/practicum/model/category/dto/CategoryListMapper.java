package ru.practicum.model.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.model.category.Category;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface CategoryListMapper {

    List<CategoryResponseDTO> toDTOList(List<Category> categories);
}
