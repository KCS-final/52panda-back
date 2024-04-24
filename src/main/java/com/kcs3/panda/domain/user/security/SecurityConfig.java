package com.kcs3.panda.domain.user.security;

import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        // OAuth 2.0 인증 요청을 처리하기 위한 기본 요청 리졸버 생성
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
                new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");

        // HTTP 보안 설정
        http
                // 요청을 인증하고 권한을 부여하는 부분
                .authorizeHttpRequests(auth -> auth
                        // 로그인 및 OAuth2 관련 경로는 모든 사용자에게 허용
                        .requestMatchers("/login/**", "/oauth2/**").permitAll()
                        // 그 외 모든 요청은 인증된 사용자에게만 허용
                        .anyRequest().authenticated()
                )
                // OAuth 2.0 로그인 설정
                .oauth2Login(oauth2Login -> oauth2Login
                        // 인증 요청 리졸버 설정
                        .authorizationEndpoint(authEndpoint -> authEndpoint
                                .authorizationRequestResolver(authorizationRequestResolver)
                        )
                        // 로그인 성공 후 리디렉션할 URL 설정
                        .defaultSuccessUrl("/home", true)  // 이 위치에 배치
                );
        // 보안 설정을 반환
        return http.build();
    }
}
