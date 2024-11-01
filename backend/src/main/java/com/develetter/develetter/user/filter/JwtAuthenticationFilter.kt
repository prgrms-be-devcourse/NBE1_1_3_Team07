package com.develetter.develetter.user.filter

import com.develetter.develetter.user.provider.JwtProvider
import com.develetter.develetter.user.repository.UserRepository
import com.example.demo.user.global.entity.UserEntity
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import org.slf4j.LoggerFactory
import java.io.IOException

/**
 * JWT 인증 필터 클래스.
 * 요청 시마다 실행되며, JWT 토큰을 파싱하고 유효성 검증을 통해
 * 사용자 정보를 인증 처리하는 역할을 담당.
 */
@Component
class JwtAuthenticationFilter(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            // Authorization 헤더에서 Bearer 토큰 파싱
            val token = parseBearerToken(request)

            // 토큰이 없거나 유효하지 않은 경우 필터 체인 계속 진행
            val userId = token?.let { jwtProvider.validate(it) }
            if (userId == null) {
                filterChain.doFilter(request, response)
                return
            }

            // 사용자 ID로 사용자 정보 조회
            val userEntity: UserEntity = userRepository.findById(userId) ?: run {
                filterChain.doFilter(request, response)
                return
            }

            val role = userEntity.role // 역할: ROLE_USER 또는 ROLE_ADMIN

            // 사용자 권한 설정
            val authorities: List<GrantedAuthority> = listOf(SimpleGrantedAuthority(role))

            // 인증 객체 생성 및 SecurityContext에 설정
            val authenticationToken: AbstractAuthenticationToken = UsernamePasswordAuthenticationToken(userId, null, authorities)
            authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().authentication = authenticationToken

        } catch (e: Exception) {
            log.info("Invalid JWT token: {}", e.message)
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response)
    }

    /**
     * 요청 헤더에서 Bearer 토큰을 파싱하여 반환.
     * @return Bearer 토큰 문자열, 없을 경우 null 반환
     */
    private fun parseBearerToken(request: HttpServletRequest): String? {
        val authorization = request.getHeader("Authorization") ?: return null

        return if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            authorization.substring(7)
        } else null
    }
}