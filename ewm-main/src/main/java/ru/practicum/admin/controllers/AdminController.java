package ru.practicum.admin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.services.AdminUserService;
import ru.practicum.client.StatisticClient;
import ru.practicum.dto.StatHitRequestDTO;
import ru.practicum.models.user.dto.RequestUserDTO;
import ru.practicum.models.user.dto.ResponseUserDTO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

/*    @Autowired
    private final StatisticClient statisticClient;*/

    @Autowired
    private final AdminUserService adminUserService;

    @PostMapping("/users")
    public ResponseUserDTO addUser(@RequestBody RequestUserDTO dto, HttpServletRequest request) {
//        hitStatistic(request);
        return adminUserService.addUser(dto);
    }

    /*private void hitStatistic(HttpServletRequest request) {
        StatHitRequestDTO dto = new StatHitRequestDTO();
        dto.setApp("ewm-main-service");
        dto.setIp(request.getRemoteAddr());
        dto.setUri(request.getRequestURI());
        dto.setTimestamp(LocalDateTime.now());
        statisticClient.hitUri(dto);
    }*/
}
