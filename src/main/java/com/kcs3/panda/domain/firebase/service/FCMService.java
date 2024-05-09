package com.kcs3.panda.domain.firebase.service;

import com.kcs3.panda.domain.firebase.service.NotificationService;
import com.kcs3.panda.global.auth.LoginUser;
import com.kcs3.panda.global.auth.UserSession;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class NotificationApiController {

    private final NotificationService notificationService;

    public NotificationApiController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String token, @LoginUser UserSession userSession) {
        notificationService.register(userSession.getId(), token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@LoginUser UserSession userSession, HttpSession httpSession) {
        loginService.logout(userSession.getId());
        notificationService.deleteToken(userSession.getId());
        httpSession.removeAttribute(USER_SESSION_KEY);
        return ResponseEntity.ok().build();

    }


}