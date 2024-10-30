package com.develetter.develetter.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(AbstractHttpConfigurer::disable) // CORS 일단 비활성화. 추후 프론트와 연결할 때 프론트 서버 CORS만 풀고 나머지는 불허해야 함.
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화. 추후 CSRF 공격을 막고 싶으면 여기 활성화 하고 쿠키 설정을 해줘야 함.
                .httpBasic(AbstractHttpConfigurer::disable) // Basic 인증 환경이 아니라 JWT 토큰이니까 Basic 모드 비활성화
                .sessionManagement(AbstractHttpConfigurer::disable) // session 기반 인증 비활성화 (JWT 기반)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("**").permitAll()
                )
                .build();
    }
}
