package com.kcs3.panda.domain.user.auth.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.kcs3.panda.domain.user.repository.UserRepository;
import com.kcs3.panda.domain.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller  // 클래스 선언에 위치
public class OAuthController {

    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);  // 클래스 수준에 위치

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/oauth2/callback")  // 메서드 수준에 위치
    public String handleOAuthCallback(OAuth2AuthenticationToken authentication, Model model) {
        logger.info("OAuth 인증 성공. 사용자 정보 추출.");
        String userEmail = authentication.getPrincipal().getAttribute("email");
        String userNickname = authentication.getPrincipal().getAttribute("name");

        User existingUser = userRepository.findByUserEmail(userEmail);
        if (existingUser == null) {
            User newUser = new User();
            newUser.setUserEmail(userEmail);
            newUser.setUserNickname(userNickname);
            try {
                User savedUser = userRepository.save(newUser);
                logger.info("User saved: {}", savedUser);
            } catch (Exception e) {
                logger.error("Error saving user", e);
            }
            logger.info("새로운 사용자 생성 및 저장: {}", newUser);
        } else {
            logger.info("기존 사용자 발견: {}", existingUser);
        }
        return "home";
    }
}
