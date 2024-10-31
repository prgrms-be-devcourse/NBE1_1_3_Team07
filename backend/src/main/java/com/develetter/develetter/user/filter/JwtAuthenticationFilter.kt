package com.develetter.develetter.user.filter;

import com.develetter.develetter.user.global.entity.UserEntity;
import com.develetter.develetter.user.provider.JwtProvider;
import com.develetter.develetter.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JWT 인증 필터 클래스.
 * 요청 시마다 실행되며, JWT 토큰을 파싱하고 유효성 검증을 통해
 * 사용자 정보를 인증 처리하는 역할을 담당.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//todo
    private final UserRepository userRepository;  // 사용자 정보를 조회 repository
    private final JwtProvider jwtProvider;        // JWT 토큰 제공 및 검증 provider

    /**
     * 요청마다 JWT 토큰을 확인하고, 유효한 토큰인 경우 사용자 인증 처리.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Authorization 헤더에서 Bearer 토큰 파싱
            String token = parseBearerToken(request);

            // 토큰이 없을 경우 필터 체인 계속 진행
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 유효성 검사 및 사용자 ID 추출
            String accountId = jwtProvider.validate(token);
            if (accountId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 사용자 ID로 사용자 정보 조회
            UserEntity userEntity = userRepository.findByAccountId(accountId);
            String role = userEntity.getRole(); // 역할: ROLE_USER 또는 ROLE_ADMIN

            // 사용자 권한 설정
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            // 인증 객체 생성 및 SecurityContext에 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(accountId, null, authorities);

            // 요청의 상세 정보 설정
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 설정
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception e) {
            log.info("Invalid JWT token.{}", e);
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 요청 헤더에서 Bearer 토큰을 파싱하여 반환.
     * @return Bearer 토큰 문자열, 없을 경우 null 반환
     */
    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더가 존재하는지 확인
        boolean hasAuthorization = StringUtils.hasText(authorization);
        if (!hasAuthorization) return null;

        // Bearer 토큰인지 확인
        boolean isBearer = authorization.startsWith("Bearer ");
        if (!isBearer) return null;

        // "Bearer " 이후의 토큰 문자열 반환
        String token = authorization.substring(7);
        return token;
    }
}
