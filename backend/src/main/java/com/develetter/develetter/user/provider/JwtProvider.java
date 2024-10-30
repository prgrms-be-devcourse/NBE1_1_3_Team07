package com.develetter.develetter.user.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스.
 * 주어진 사용자 ID로 JWT 토큰을 생성 -> 토큰의 유효성을 검증하여 사용자 ID를 반환
 */
/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스.
 * 주어진 사용자 ID로 JWT 토큰을 생성 -> 토큰의 유효성을 검증하여 사용자 ID를 반환
 */
@Component
@Slf4j
public class JwtProvider {

    // JWT 토큰의 서명에 사용할 비밀 키
    @Value("${secret-key}")
    private String secretKey;

    /**
     * 주어진 사용자 ID로 JWT 토큰을 생성하는 메서드.
     * 토큰은 1시간 동안 유효하며, 사용자 ID를 서브젝트로 설정.
     * @param userId 사용자 ID
     * @return 생성된 JWT 토큰 문자열
     */
    public String create(String userId, String role) {
        // 토큰 만료 시간 설정 (1시간 후)(시간늘림 가능)
        Date expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));

        // 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // JWT 토큰 생성
        String jwt = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 및 키 설정
                .setSubject(userId)  // 사용자 ID를 서브젝트로 설정
                .claim("role", role)
                .setIssuedAt(new Date())  // 토큰 발행 시간 설정
                .setExpiration(expireDate)  // 토큰 만료 시간 설정
                .compact();  // 토큰 생성 및 압축

        return jwt;
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증 -> 유효한 경우 사용자 ID를 반환
     * @param jwt JWT 토큰
     * @return 유효한 경우 사용자 ID 반환, 유효하지 않은 경우 null 반환
     */
    public String validate(String jwt) {
        String subject = null;
        // 비밀 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            // JWT 토큰의 서명을 검증하고, 서브젝트(사용자 ID)를 추출
            subject = Jwts.parserBuilder()
                    .setSigningKey(key)  // 서명 검증에 사용할 키 설정
                    .build()
                    .parseClaimsJws(jwt)  // JWT 토큰 검증 및 파싱
                    .getBody()
                    .getSubject();  // 서브젝트(사용자 ID) 추출
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JWT 검증 실패: {}", e.getMessage());
            return null;
        }
        return subject;  // 유효한 경우 사용자 ID 반환
    }
}
