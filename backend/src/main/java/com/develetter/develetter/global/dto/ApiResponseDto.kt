package com.develetter.develetter.global.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ApiResponseDto<T>(
    val status: Int = HttpStatus.OK.value(),
    val message: String = "",
    val data: T? = null,
    val timeStamp: LocalDateTime = LocalDateTime.now()
) {
    @JvmOverloads
    constructor(message: String, data: T? = null) : this(
        status = HttpStatus.OK.value(),
        message = message,
        data = data,
        timeStamp = LocalDateTime.now()
    )

    @JvmOverloads
    constructor(status: Int, message: String, data: T? = null) : this(
        status = status,
        message = message,
        data = data,
        timeStamp = LocalDateTime.now()
    )

    fun getHttpStatus(): HttpStatus {
        return try {
            HttpStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
