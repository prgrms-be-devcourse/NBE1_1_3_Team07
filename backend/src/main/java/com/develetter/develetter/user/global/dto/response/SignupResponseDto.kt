package com.develetter.develetter.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.develetter.develetter.user.global.dto.LogInResponseDto
import lombok.Getter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@Getter
class SignupResponseDto private constructor(role: String?) : LogInResponseDto() {
    private val role: String?

    init {
        this.role = role
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun duplicateId(): ResponseEntity<LogInResponseDto?> {
            val responseBody = LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<LogInResponseDto?>(responseBody)
        }

        @kotlin.jvm.JvmStatic
        fun certificationFail(): ResponseEntity<LogInResponseDto?> {
            val responseBody = LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body<LogInResponseDto?>(responseBody)
        }

        fun wrongRole(): ResponseEntity<LogInResponseDto?> {
            val responseBody = LogInResponseDto(ResponseCode.WRONG_ROLE, ResponseMessage.WRONG_ROLE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<LogInResponseDto?>(responseBody)
        }
    }
}
