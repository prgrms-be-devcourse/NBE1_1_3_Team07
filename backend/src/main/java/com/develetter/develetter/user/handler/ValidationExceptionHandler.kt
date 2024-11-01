package com.develetter.develetter.user.handler

import com.example.demo.user.global.dto.LogInResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ValidationExceptionHandler {

    @ExceptionHandler(value = [MethodArgumentNotValidException::class, HttpMessageNotReadableException::class])
    fun validationExceptionHandler(e: Exception): ResponseEntity<LogInResponseDto> {
        return LogInResponseDto.validationFail()
    }
}