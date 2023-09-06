package ru.practicum.model.user.dto;

import org.mapstruct.Mapper;
import ru.practicum.model.user.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {

    List<UserResponseDTO> toDTOList(List<User> users);
}
