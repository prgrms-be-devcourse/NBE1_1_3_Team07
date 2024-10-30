package com.develetter.develetter.global.exception;

import com.develetter.develetter.global.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ApiResponseDto<Void> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        log.error("BadRequestException: {}", e.getMessage());
        return new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponseDto<Void> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("IllegalArgumentException: {}", e.getMessage());
        return new ApiResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    // 모든 예외 처리 (Global Exception Handler)
    @ExceptionHandler(Exception.class)
    public ApiResponseDto<Void> handleAllExceptions(Exception e, HttpServletRequest request) {
        log.error("Exception: {}", e.getMessage());
        return new ApiResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
    }
}