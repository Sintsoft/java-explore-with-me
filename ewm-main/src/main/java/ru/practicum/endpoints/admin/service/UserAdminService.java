package ru.practicum.endpoints.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.user.dto.UserListMapper;
import ru.practicum.model.user.dto.UserMapper;
import ru.practicum.model.user.dto.UserRequestDTO;
import ru.practicum.model.user.dto.UserResponseDTO;
import ru.practicum.model.user.repository.UserStorage;
import ru.practicum.utility.exceptions.EwmInvalidRequestParameterException;
import ru.practicum.utility.validation.CommonVaidationChecks;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAdminService {

    @Autowired
    private final UserStorage userStorage;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final UserListMapper userListMapper;

    public UserResponseDTO addUser(UserRequestDTO dto) {
        log.debug("LEVEL - Storage. METHOD - addUser. Entered");
        return userMapper.toDTO(
                    userStorage.saveUser(
                        userMapper.fromDTO(dto)));
    }

    public List<UserResponseDTO> getUsers(int from, int size, List<Long> ids) {
        log.debug("LEVEL - Storage. METHOD - addUser. Entered");
        if (ids != null && !ids.isEmpty()) {
            return userListMapper.toDTOList(
                    userStorage.getUsersByListId(ids));
        }
        CommonVaidationChecks.paginationParamsValidation(from,size);
        return userListMapper.toDTOList(
                userStorage.getUsers(from, size));
    }

    public void deleteUser(Long id) {
        log.debug("LEVEL - Storage. METHOD - addUser. Entered");
        if (id == null || id < 1) {
            log.info("Incorrect user id. It must be > 0");
            throw new EwmInvalidRequestParameterException("Incorrect user id. It must be > 0");
        }
        userStorage.removeUser(id);
    }
}
