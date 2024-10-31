package com.example.demo.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.develetter.develetter.user.global.dto.LogInResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CheckCertificationResponseDto : LogInResponseDto() {

    companion object {
        fun certificationFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody)
        }
    }
}