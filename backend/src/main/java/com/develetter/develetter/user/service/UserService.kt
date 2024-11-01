package com.develetter.develetter.user.service

import com.develetter.develetter.user.global.dto.request.*
import com.example.demo.user.global.dto.LogInResponseDto
import com.example.demo.user.global.dto.response.CheckCertificationResponseDto
import com.example.demo.user.global.dto.response.DeleteIdResponseDto
import com.example.demo.user.global.dto.response.RegisterSubscribeResponseDto
import com.example.demo.user.global.dto.response.SigninResponseDto
import com.example.demo.user.global.dto.response.SignupResponseDto
import com.example.demo.user.global.entity.UserEntity
import org.springframework.http.ResponseEntity

interface UserService {
    fun idCheck(dto: IdCheckRequestDto): ResponseEntity<out LogInResponseDto>
    fun emailCertification(dto: EmailCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun checkCertification(dto: CheckCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun signUp(dto: SignupRequestDto): ResponseEntity<out LogInResponseDto>
    fun signIn(dto: SigninRequestDto): ResponseEntity<out LogInResponseDto>
    fun deleteId(dto: DeleteIdRequestDto): ResponseEntity<out LogInResponseDto>
    fun registerSubscribe(dto: RegisterSubscribeRequestDto): ResponseEntity<out LogInResponseDto>
    fun getEmailByUserId(id: Long): String
    fun getAllUsers(): List<UserEntity>
}