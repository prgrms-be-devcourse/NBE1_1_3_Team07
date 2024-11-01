package com.develetter.develetter.user.filter

import com.develetter.develetter.user.provider.JwtProvider
import com.develetter.develetter.user.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
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
            val token = parseBearerToken(request)

            if (token == null) {
                filterChain.doFilter(request, response)
                return
            }

            val accountId = jwtProvider.validate(token)
            if (accountId == null) {
                filterChain.doFilter(request, response)
                return
            }

            val userEntity = userRepository.findByAccountId(accountId)
                ?: run {
                    filterChain.doFilter(request, response)
                    return
                }
            val role = userEntity.role

            val authorities = listOf<GrantedAuthority>(SimpleGrantedAuthority(role))

            val authenticationToken = UsernamePasswordAuthenticationToken(accountId, null, authorities).apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
            }

            SecurityContextHolder.getContext().authentication = authenticationToken
        } catch (e: Exception) {
            log.info("Invalid JWT token: {}", e.message)
        }

        filterChain.doFilter(request, response)
    }

    /**
     * 요청 헤더에서 Bearer 토큰을 파싱하여 반환.
     * @return Bearer 토큰 문자열, 없을 경우 null 반환
     */
    private fun parseBearerToken(request: HttpServletRequest): String? {
        val authorization = request.getHeader("Authorization")
        return if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            authorization.substring(7)
        } else {
            null
        }
    }
}