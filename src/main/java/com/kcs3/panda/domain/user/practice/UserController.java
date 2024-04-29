package com.kcs3.panda.domain.user.practice;

import com.kcs3.panda.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 추가 엔드포인트
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestParam String userNickname, @RequestParam String userEmail) {
        try {
            User newUser = userService.addUser(userNickname, userEmail);
            return ResponseEntity.ok(newUser); // 성공 시 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 중복 오류 등 처리
        }
    }
}
