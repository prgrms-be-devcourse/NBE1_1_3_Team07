package com.example.demo.user.global.dto.response

import com.example.demo.user.global.dto.LogInResponseDto


class RegisterSubscribeResponseDto : LogInResponseDto() {
    companion object {
        fun success(): RegisterSubscribeResponseDto {
            return RegisterSubscribeResponseDto()
        }
    }
}