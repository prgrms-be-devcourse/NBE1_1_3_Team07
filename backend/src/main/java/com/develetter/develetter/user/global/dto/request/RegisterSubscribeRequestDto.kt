package com.develetter.develetter.user.global.dto.request

data class RegisterSubscribeRequestDto(
    val userId: Long = 0,
    val subscribeType: String = ""
)