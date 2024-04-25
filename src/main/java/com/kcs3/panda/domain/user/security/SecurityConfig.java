
package com.kcs3.panda.domain.user.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  // 권한 요구사항 설정
                .anyRequest().authenticated()  // 모든 요청에 대해 인증 필요
                .and()  // 다음 단계로 이동
                .oauth2Login(oauth2Login -> {  // OAuth2 로그인 설정
                    oauth2Login
                            .loginPage("/login")  // 사용자 정의 로그인 페이지
                            .permitAll();  // 모든 사용자에게 로그인 페이지 허용
                });
        return http.build();  // 설정 적용
    }
}