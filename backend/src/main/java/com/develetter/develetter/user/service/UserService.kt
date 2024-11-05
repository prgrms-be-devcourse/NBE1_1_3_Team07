package com.develetter.develetter.user.service

import com.develetter.develetter.user.global.dto.LogInResponseDto
import com.develetter.develetter.user.global.dto.request.*
import org.springframework.http.ResponseEntity
import com.develetter.develetter.user.global.entity.UserEntity


interface UserService {
    fun idCheck(dto: IdCheckRequestDto): ResponseEntity<LogInResponseDto>
    fun emailCertification(dto: EmailCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun checkCertification(dto: CheckCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun signUp(dto: SignupRequestDto): ResponseEntity<out LogInResponseDto?>
    fun signIn(dto: SigninRequestDto): ResponseEntity<out LogInResponseDto>
    fun deleteId(dto: DeleteIdRequestDto): ResponseEntity<out LogInResponseDto>
    fun registerSubscribe(dto: RegisterSubscribeRequestDto): ResponseEntity<out LogInResponseDto>
    fun getEmailByUserId(id: Long?): String
    fun getAllUsers(): List<UserEntity>
}