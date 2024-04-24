package com.kcs3.panda.domain.user.auth.controller;

import com.kcs3.panda.domain.user.entity.User;
import com.kcs3.panda.domain.user.jwt.JwtUtil;
import com.kcs3.panda.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        userRepository.save(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            // JWT 토큰 생성
            String token = jwtUtil.generateToken(user.getUsername());
            return "JWT Token: " + token;
        }
        return "Invalid credentials";
    }
}
