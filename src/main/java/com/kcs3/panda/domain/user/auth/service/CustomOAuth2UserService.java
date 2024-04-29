package com.kcs3.panda.domain.user.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.kcs3.panda.domain.user.entity.User;
import com.kcs3.panda.domain.user.repository.UserRepository;

import java.util.*;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        logger.info("Loading OAuth2 user information...12345");

        // 토큰 값 가져오기
        String accessToken = userRequest.getAccessToken().getTokenValue();

        // 토큰 값이 제대로 가져와졌는지 확인
        if (accessToken == null || accessToken.isBlank()) {
            logger.error("Access token is invalid or missing.12345");
            throw new OAuth2AuthenticationException("Invalid or missing access token.12345");
        }

        logger.debug("Access token retrieved 12345: {}", accessToken);

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("sub", accessToken),
                "sub"
        );

        String nickname = (String) oAuth2User.getAttribute("name");
        logger.debug("Fetched nickname12345: {}", nickname);

        User user = userRepository.findByUserNickname(nickname);
        if (user == null) {
            user = new User();
            user.setUserNickname(nickname);
            user.setUserEmail("default@example.com");

            logger.info("Saving new user with nickname12345: {} and email12345: {}", nickname, user.getUserEmail());
            userRepository.save(user);
        }

        return oAuth2User;
    }
}

