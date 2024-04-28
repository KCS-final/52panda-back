package com.kcs3.panda.domain.user.auth.service;

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

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2User 정보를 가져오기
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 권한
                Map.of("sub", userRequest.getAccessToken().getTokenValue()),   // 사용자 속성
                "sub"                                                          // 주 식별자
        );

        String nickname = (String) oAuth2User.getAttribute("name"); // 구글에서 닉네임 가져오기

        // 닉네임으로 사용자 조회
        User user = userRepository.findByUserNickname(nickname);

        // 새로운 사용자라면 저장
        if (user == null) {
            user = new User();
            user.setUserNickname(nickname);
            userRepository.save(user);
        }

        return oAuth2User;
    }
}
