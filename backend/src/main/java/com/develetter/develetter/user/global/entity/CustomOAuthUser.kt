package com.develetter.develetter.user.global.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * OAuth2User 커스텀을 위한 클래스
 */
class CustomOAuthUser(
    val id: Long, // Long 타입의 사용자 ID
    private val accountId: String
) : OAuth2User {

    override fun getAttributes(): Map<String, Any>? {
        return null
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return null
    }

    override fun getName(): String {
        return accountId
    }
}