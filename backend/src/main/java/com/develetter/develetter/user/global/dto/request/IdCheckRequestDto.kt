package com.develetter.develetter.user.global.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class IdCheckRequestDto(
    @field:Email
    @field:NotBlank
    val email: String = ""
)