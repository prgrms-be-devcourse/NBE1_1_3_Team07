package com.develetter.develetter.user.global.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SigninRequestDto(
    @field:Email
    @field:NotBlank
    val email: String = "",

    @field:NotBlank
    val password: String = ""
)