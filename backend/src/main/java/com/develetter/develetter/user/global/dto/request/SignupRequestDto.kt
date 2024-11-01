package com.develetter.develetter.user.global.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequestDto(
    @field:Email
    @field:NotBlank
    val email: String = "",

    @field:NotBlank
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$")
    val password: String = "",

    @field:NotBlank
    val certificationNumber: String = ""
)