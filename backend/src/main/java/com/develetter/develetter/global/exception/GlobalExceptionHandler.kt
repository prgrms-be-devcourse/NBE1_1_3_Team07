package com.develetter.develetter.global.exception

import com.develetter.develetter.global.dto.ApiResponseDto
import jakarta.servlet.http.HttpServletRequest
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.slf4j.LoggerFactory

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(e: BadRequestException, request: HttpServletRequest): ApiResponseDto<Void?> {
        log.error("BadRequestException: {}", e.message)
        return ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.message, null)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException, request: HttpServletRequest): ApiResponseDto<Void?> {
        log.error("IllegalArgumentException: {}", e.message)
        return ApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.message, null)
    }

    // 모든 예외 처리 (Global Exception Handler)
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(e: Exception, request: HttpServletRequest): ApiResponseDto<Void?> {
        log.error("Exception: {}", e.message)
        return ApiResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: ${e.message}", null)
    }
}
