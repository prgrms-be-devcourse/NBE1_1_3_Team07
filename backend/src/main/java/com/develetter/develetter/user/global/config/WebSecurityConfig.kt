package com.develetter.develetter.user.global.config

import com.develetter.develetter.user.filter.JwtAuthenticationFilter
import com.develetter.develetter.user.handler.OAuthSuccessHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.io.IOException

/**
 * Spring Security 설정 클래스.
 * JWT 기반 인증과 OAuth2 로그인 설정을 담당하며, CORS 및 CSRF 설정도 포함.
 */
@Configuration
@EnableWebSecurity
open class WebSecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val oAuth2UserService: DefaultOAuth2UserService,
    private val oAuthSuccessHandler: OAuthSuccessHandler
) {

    /**
     * Spring Security 필터 체인 설정.
     * CORS, CSRF, 세션 관리, 권한 검증, OAuth2 로그인 설정 등을 구성.
     */
    @Bean
    open fun configure(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeRequests { auth ->
                auth
                    .requestMatchers("/", "/api/auth/**", "/oauth2/**").permitAll()
                    .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .authorizationEndpoint { it.baseUri("/api/auth/oauth2") }
                    .redirectionEndpoint { it.baseUri("/oauth2/callback/*") }
                    .userInfoEndpoint { it.userService(oAuth2UserService) }
                    .successHandler(oAuthSuccessHandler)
            }
            .exceptionHandling { it.authenticationEntryPoint(FailedAuthenticationEntryPoint()) }
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    /**
     * CORS 설정.
     * 모든 도메인, 헤더, 메소드를 허용하는 설정을 적용.
     */
    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.addAllowedOrigin("*")
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", corsConfiguration)
        return source
    }
}

/**
 * 인증 실패 시 처리하는 엔트리 포인트 클래스.
 * 인증되지 않은 요청이 들어올 경우 403 Forbidden 상태와 JSON 메시지를 응답으로 반환.
 */
class FailedAuthenticationEntryPoint : AuthenticationEntryPoint {

    /**
     * 인증 실패 시 호출되는 메서드.
     */
    @Throws(IOException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_FORBIDDEN
        response.writer.write("{\"code\":\"NP\",\"message\":\"No Permission.\"}")
    }
}