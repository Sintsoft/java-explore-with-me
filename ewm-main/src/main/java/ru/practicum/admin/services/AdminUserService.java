package ru.practicum.admin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.models.user.dto.RequestUserDTO;
import ru.practicum.models.user.dto.ResponseUserDTO;
import ru.practicum.models.user.model.User;
import ru.practicum.models.user.vault.UserStroage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    @Autowired
    private final UserStroage userStroage;

    public ResponseUserDTO addUser(RequestUserDTO dto) {
        return userStroage.saveUser(User.fromDTO(dto)).toDTO();
    }

    public List<ResponseUserDTO> getUsers(int from, int size) {
        return userStroage.getUsers(from, size).stream().map(user -> user.toDTO()).collect(Collectors.toList());
    }

    public void deleteUser(long id) {
        userStroage.removeUser(id);
    }
}
