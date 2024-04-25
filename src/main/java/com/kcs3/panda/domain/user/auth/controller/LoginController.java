package com.kcs3.panda.domain.user.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // 'templates' 폴더 안의 'login.html'을 찾음
    }
}

