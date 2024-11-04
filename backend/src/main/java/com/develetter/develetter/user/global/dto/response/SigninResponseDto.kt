package com.develetter.develetter.user.global.dto.response

import LogInResponseDto
import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class SigninResponseDto private constructor(
    val token: String,
    val expirationTime: Int = 3600, // 1 hour
    val role: String
) : LogInResponseDto() {

    companion object {
        fun success(token: String, role: String): ResponseEntity<SigninResponseDto> {
            val responseBody = SigninResponseDto(token, expirationTime = 3600,role)
            return ResponseEntity.status(HttpStatus.OK).body(responseBody)
        }

        fun signInFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody)
        }
    }
}