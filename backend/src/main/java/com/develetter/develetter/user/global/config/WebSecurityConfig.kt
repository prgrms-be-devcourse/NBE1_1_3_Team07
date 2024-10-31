package com.develetter.develetter.user.global.config;

import com.develetter.develetter.user.filter.JwtAuthenticationFilter;
import com.develetter.develetter.user.handler.OAuthSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Endpoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

/**
 * Spring Security 설정 클래스.
 * JWT 기반 인증과 OAuth2 로그인 설정을 담당하며, CORS 및 CSRF 설정도 포함.
 */
@Configurable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    // JWT 인증 필터
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // OAuth2 사용자 정보 서비스
    private final DefaultOAuth2UserService oAuth2UserService;

    // OAuth2 성공 핸들러
    private final OAuthSuccessHandler oAuthSuccessHandler;

    /**
     * Spring Security 필터 체인 설정.
     * CORS, CSRF, 세션 관리, 권한 검증, OAuth2 로그인 설정 등을 구성.
     * @param httpSecurity HttpSecurity 객체
     * @return SecurityFilterChain 객체
     */
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource()) // CORS 설정
                )
                .csrf(CsrfConfigurer::disable) // CSRF 비활성화
                .httpBasic(HttpBasicConfigurer::disable) // HTTP Basic 인증 비활성화
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화 (JWT 사용)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/api/auth/**", "/oauth2/**").permitAll() // 인증 없이 접근 가능
                        .requestMatchers("/api/user/**").hasAnyRole("USER","ADMIN") // USER, ADMIN둘다 가능
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // ADMIN 역할 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/auth/oauth2")) // OAuth2 로그인 엔드포인트 설정
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*")) // 리디렉션 엔드포인트 설정
//                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/oauth2/authorization")) // OAuth2 로그인 URL
//                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/*")) // 리디렉션 URL
                        .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService)) // 사용자 정보 서비스 설정
                        .successHandler(oAuthSuccessHandler) // 성공 핸들러 설정
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())) // 인증 실패 시 처리
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return httpSecurity.build();
    }

    /**
     * CORS 설정.
     * 모든 도메인, 헤더, 메소드를 허용하는 설정을 적용.
     * @return CorsConfigurationSource 객체
     */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 모든 Origin 허용
        corsConfiguration.addAllowedHeader("*"); // 모든 Header 허용
        corsConfiguration.addAllowedMethod("*"); // 모든 Method 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfiguration); // API 경로에 대해 CORS 설정 적용

        return source;
    }
}

/**
 * 인증 실패 시 처리하는 엔트리 포인트 클래스.
 * 인증되지 않은 요청이 들어올 경우 403 Forbidden 상태와 JSON 메시지를 응답으로 반환.
 */
class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증 실패 시 호출되는 메서드.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 반환
        response.getWriter().write("{\"code\":\"NP\",\"message\":\"No Permission.\"}"); // 인증 실패 메시지 반환
    }

}
