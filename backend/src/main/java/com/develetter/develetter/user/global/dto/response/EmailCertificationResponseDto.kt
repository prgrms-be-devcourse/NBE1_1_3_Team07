package com.example.demo.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.develetter.develetter.user.global.dto.LogInResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class EmailCertificationResponseDto : LogInResponseDto() {

    companion object {
        fun duplicateId(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
        }

        fun mailSendFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody)
        }
    }
}