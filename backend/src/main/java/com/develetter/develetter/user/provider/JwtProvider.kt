package com.develetter.develetter.user.provider

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스.
 * 주어진 사용자 ID로 JWT 토큰을 생성 -> 토큰의 유효성을 검증하여 사용자 ID를 반환
 */
@Component
class JwtProvider {

    @Value("\${secret-key}")
    private lateinit var secretKey: String

    private val log = LoggerFactory.getLogger(JwtProvider::class.java)

    /**
     * 주어진 사용자 ID와 역할로 JWT 토큰을 생성하는 메서드.
     * 토큰은 1시간 동안 유효하며, 사용자 ID를 서브젝트로 설정.
     * @param id 사용자 ID
     * @param role 사용자 역할
     * @return 생성된 JWT 토큰 문자열
     */
    fun create(id: Long, role: String): String {
        // 토큰 만료 시간 설정 (1시간 후)
        val expireDate = Date.from(Instant.now().plus(1, ChronoUnit.HOURS))

        // 비밀 키 생성
        val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        // JWT 토큰 생성
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)  // 서명 알고리즘 및 키 설정
            .setSubject(id.toString())  // 사용자 ID를 서브젝트로 설정
            .claim("role", role)  // 역할 클레임 설정
            .setIssuedAt(Date())  // 토큰 발행 시간 설정
            .setExpiration(expireDate)  // 토큰 만료 시간 설정
            .compact()  // 토큰 생성 및 압축
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검증 -> 유효한 경우 사용자 ID를 반환
     * @param jwt JWT 토큰
     * @return 유효한 경우 사용자 ID 반환 (Long 타입), 유효하지 않은 경우 null 반환
     */
    fun validate(jwt: String): Long? {
        val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        return try {
            val subject = Jwts.parserBuilder()
                .setSigningKey(key)  // 서명 검증에 사용할 키 설정
                .build()
                .parseClaimsJws(jwt)  // JWT 토큰 검증 및 파싱
                .body
                .subject  // 서브젝트(사용자 ID) 추출

            subject.toLongOrNull()  // String 타입의 subject를 Long으로 변환
        } catch (e: Exception) {
            log.info("JWT 검증 실패: {}", e.message)
            null
        }
    }
}