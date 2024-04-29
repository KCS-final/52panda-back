// OAuthController.java
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

@Controller
public class OAuthController {
    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/oauth2/callback")
    public String handleOAuthCallback(OAuth2AuthenticationToken authentication, Model model) {
        // 로그 시작
        logger.info("OAuth 인증 성공. 사용자 정보 추출 중...");

        // 사용자 정보 추출
        String userEmail = authentication.getPrincipal().getAttribute("email");
        String userNickname = authentication.getPrincipal().getAttribute("name");
        logger.info("사용자 이메일: {}, 닉네임: {}", userEmail, userNickname);

        // 기존 사용자 확인
        User existingUser = userRepository.findByUserEmail(userEmail);
        if (existingUser == null) {
            // 새로운 사용자 생성
            User newUser = new User();
            newUser.setUserEmail(userEmail);
            newUser.setUserNickname(userNickname);
            logger.info("새로운 사용자 생성: {}", newUser);

            // 사용자 저장 시도
            try {
                logger.info("사용자 저장 시도 중...");
                User savedUser = userRepository.save(newUser);
                logger.info("사용자 저장 성공: {}", savedUser);
            } catch (Exception e) {
                logger.error("사용자 저장 실패. 오류 메시지: {}, 스택 트레이스: {}", e.getMessage(), e);
                return "error";  // 오류 페이지로 리디렉션
            }
        } else {
            logger.info("기존 사용자 발견: {}", existingUser);
        }

        // 인증 성공 시 리디렉션
        return "home";
    }
}
