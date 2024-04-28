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
        logger.info("Loading OAuth2 user information...");

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("sub", userRequest.getAccessToken().getTokenValue()),
                "sub"
        );

        String nickname = (String) oAuth2User.getAttribute("name");
        logger.debug("Fetched nickname: {}", nickname);

        User user = userRepository.findByUserNickname(nickname);

        if (user == null) {
            logger.info("User not found. Creating a new user with nickname: {}", nickname);
            user = new User();
            user.setUserNickname(nickname);
            userRepository.save(user);
            logger.info("New user saved successfully.");
        } else {
            logger.info("Existing user found with nickname: {}", nickname);
        }

        return oAuth2User;
    }
}
