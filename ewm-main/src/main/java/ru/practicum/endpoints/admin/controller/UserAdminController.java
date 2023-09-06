package ru.practicum.endpoints.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.endpoints.admin.service.UserAdminService;
import ru.practicum.model.user.dto.UserRequestDTO;
import ru.practicum.model.user.dto.UserResponseDTO;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    @Autowired
    private final UserAdminService service;

    @Autowired
    private final StatisticClient statisticClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO postUser(@Valid @RequestBody UserRequestDTO dto) {
        log.debug("LEVEL - Controller. METHOD - postUser. Entered");
        return service.addUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getUsers(@RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) List<Long> ids,
                                          HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - getUsers. Entered");
        hitStatistic(request);
        return service.getUsers(from,size,ids);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("LEVEL - Controller. METHOD - deleteUser. Entered");
        service.deleteUser(userId);
    }

    private void hitStatistic(HttpServletRequest request) {
        log.debug("LEVEL - Controller. METHOD - hitStatistic. Entered");
        StatHitRequestDTO dto = new StatHitRequestDTO();
        dto.setApp("ewm-main-service");
        dto.setIp(request.getRemoteAddr());
        dto.setUri(request.getRequestURI());
        dto.setTimestamp(LocalDateTime.now());
        statisticClient.hitUri(dto);
        log.debug("LEVEL - Controller. METHOD - hitStatistic. Exiting");
    }
}
