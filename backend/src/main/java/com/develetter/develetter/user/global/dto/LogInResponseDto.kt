package com.develetter.develetter.user.global.dto

import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

open class LogInResponseDto(
    val code: String = ResponseCode.SUCCESS,
    val message: String = ResponseMessage.SUCCESS
) {
    companion object {
        fun success(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto()
            return ResponseEntity.status(HttpStatus.OK).body(responseBody)
        }

        fun databaseError(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody)
        }

        fun validationFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
        }
    }
}