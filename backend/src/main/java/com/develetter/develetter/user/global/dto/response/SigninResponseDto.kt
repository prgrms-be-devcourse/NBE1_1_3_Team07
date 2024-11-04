package com.develetter.develetter.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.develetter.develetter.user.global.dto.LogInResponseDto
import lombok.Getter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Getter
class SigninResponseDto private constructor(token: String?, role: String?) : LogInResponseDto() {
    private val token: String?
    private val expirationTime: Int
    private val role: String?

    init {
        this.token = token
        this.expirationTime = 3600 // 1 hour
        this.role = role
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun success(token: String?, role: String?): ResponseEntity<SigninResponseDto?> {
            val responseBody = SigninResponseDto(token, role)
            return ResponseEntity.status(HttpStatus.OK).body<SigninResponseDto?>(responseBody)
        }

        @kotlin.jvm.JvmStatic
        fun signInFail(): ResponseEntity<LogInResponseDto?> {
            val responseBody = LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<LogInResponseDto?>(responseBody)
        }
    }
}
