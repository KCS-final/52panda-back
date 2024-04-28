package com.kcs3.panda.domain.auction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";  // 'templates' 폴더 안의 'home.html'을 찾음
    }
}