package ru.practicum.model.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.model.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDTO(UserRequestDTO dto);

    UserResponseDTO toDTO(User user);

    UserNestedDTO toNested(User user);
}
