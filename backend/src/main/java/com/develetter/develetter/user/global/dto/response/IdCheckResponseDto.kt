package com.example.demo.user.global.dto.response

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import com.example.demo.user.global.dto.LogInResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class IdCheckResponseDto : LogInResponseDto() {

    companion object {
        fun duplicateId(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
        }

        fun success(): ResponseEntity<IdCheckResponseDto> {
            val responseBody = IdCheckResponseDto()
            return ResponseEntity.status(HttpStatus.OK).body(responseBody)
        }
    }
}