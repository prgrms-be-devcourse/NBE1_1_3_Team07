package com.develetter.develetter.user.service.implement

import com.develetter.develetter.user.global.entity.CustomOAuthUser
import com.develetter.develetter.user.repository.UserRepository
import com.example.demo.user.global.entity.UserEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuthUserServiceImpl(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    /**
     * OAuth2 인증 후 사용자 정보를 처리하는 메서드.
     * 제공자에 따라 사용자 ID를 생성하고, 해당 정보를 기반으로 사용자 엔티티를 저장.
     */
    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        // 기본 OAuth2UserService를 사용해 사용자 정보 로드
        val oAuth2User = super.loadUser(request)

        // OAuth 클라이언트를 사용해 제공자 및 클라이언트 ID 가져오기
        val oauthClientName = request.clientRegistration.clientName
        val oauthClientId = request.clientRegistration.clientId

        val password = passwordEncoder.encode(oauthClientId)
        var email = "$oauthClientId@email.com"
        var accountId: String? = null
        var userEntity: UserEntity? = null

        when (oauthClientName) {
            "kakao" -> {
                accountId = "kakao_" + oAuth2User.attributes["id"]
                userEntity = UserEntity(
                    id = null,
                    accountId = accountId,
                    password = password,
                    email = email,
                    type = "kakao",
                    role = "ROLE_USER",
                    subscription = "NO"
                )
            }
            "naver" -> {
                val responseMap = oAuth2User.attributes["response"] as Map<String, String>
                accountId = "naver_" + responseMap["id"]?.substring(0, 14)
                email = responseMap["email"] ?: "$oauthClientId@email.com"
                userEntity = UserEntity(
                    id = null,
                    accountId = accountId,
                    password = password,
                    email = email,
                    type = "naver",
                    role = "ROLE_USER",
                    subscription = "NO"
                )
            }
        }

        // 사용자 정보 저장
        userRepository.save(userEntity!!)

        // 사용자 정보를 포함한 CustomOAuthUser 객체 반환
        return CustomOAuthUser(accountId!!)
    }
}