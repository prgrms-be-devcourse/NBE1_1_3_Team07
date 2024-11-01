package com.develetter.develetter.user.global.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CheckCertificationRequestDto(
    @field:Email
    @field:NotBlank
    val email: String = "",

    @field:NotBlank
    val certificationNumber: String = ""
)