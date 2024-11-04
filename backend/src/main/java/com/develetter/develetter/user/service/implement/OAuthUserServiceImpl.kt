package com.develetter.develetter.user.service.implement

import UserEntity
import com.develetter.develetter.user.global.entity.CustomOAuthUser
import com.develetter.develetter.user.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service

/**
 * Service implementation to process OAuth2 user information.
 * Handles user information received from OAuth providers like Kakao and Naver,
 * and either saves new user data or retrieves existing user data.
 */
@Service
class OAuthUserServiceImpl(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Processes OAuth2 user information after authentication.
     * Creates or retrieves user entity based on provider information.
     * @param request OAuth2UserRequest containing client registration details
     * @return CustomOAuthUser containing user details
     */
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(request: OAuth2UserRequest): CustomOAuthUser? {
        val oAuth2User = super.loadUser(request)

        val oauthClientName = request.clientRegistration.clientName
        val oauthClientId = request.clientRegistration.clientId

        var accountId: String? = null
        val password = passwordEncoder.encode(oauthClientId)  // Encrypt ClientId as password
        var email = "$oauthClientId@example.com"  // Default email setting
        val providerType = if (oauthClientName == "kakao") "kakao" else "naver"

        // Configure accountId and email based on OAuth provider
        if (providerType == "kakao") {
            accountId = "kakao_" + oAuth2User.attributes["id"]
        } else if (providerType == "naver") {
            val responseMap = oAuth2User.attributes["response"] as Map<String, String>
            accountId = "naver_" + responseMap["id"]?.take(14)
            email = responseMap["email"] ?: email
        }

        // Find existing user by accountId or create a new one if not found
        var userEntity = accountId?.let { userRepository.findByAccountId(it) }
        if (userEntity == null) {
            userEntity = UserEntity(
                accountId = accountId!!,
                password = password,
                email = email,
                type = providerType,
                role = "ROLE_USER",
                subscription = "NO"
            )
            userEntity = userRepository.save(userEntity)
        }

        // Return CustomOAuthUser with user ID and accountId
        return accountId?.let { CustomOAuthUser(userEntity.id!!, it) }
    }
}