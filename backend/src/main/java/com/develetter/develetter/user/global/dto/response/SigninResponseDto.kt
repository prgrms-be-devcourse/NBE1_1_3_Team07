package com.example.demo.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.develetter.develetter.user.global.dto.LogInResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class SigninResponseDto(
    val token: String,
    val expirationTime: Int = 3600,
    val email: String,
    val role: String,
    val subscribe: String
) : LogInResponseDto() {

    companion object {
        fun success(token: String, role: String, email: String, subscribe: String): ResponseEntity<SigninResponseDto> {
            val responseBody = SigninResponseDto(token, 3600, role, email, subscribe)
            return ResponseEntity.status(HttpStatus.OK).body(responseBody)
        }

        fun signInFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody)
        }
    }
}