package com.develetter.develetter.user.global.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class DeleteIdRequestDto(
    @field:Email
    @field:NotBlank
    val email: String = "",

    @field:NotBlank
    val password: String = ""
)