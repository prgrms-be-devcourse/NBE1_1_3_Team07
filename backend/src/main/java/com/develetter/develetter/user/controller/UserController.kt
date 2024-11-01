package com.develetter.develetter.user.controller

import com.develetter.develetter.user.global.dto.request.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class UserController(private val userService: UserService) {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @Operation(summary = "ID 중복 체크", description = "ID 중복 체크하는 API")
    @PostMapping("/id-check")
    fun idCheck(@RequestBody @Valid requestBody: IdCheckRequestDto): ResponseEntity<out IdCheckResponseDto> {
        val response = userService.idCheck(requestBody)
        log.info("[idCheck]: {id: ${requestBody.email}}")
        return response
    }

    @Operation(summary = "이메일 인증코드 발송", description = "이메일 인증코드 발송하는 API")
    @PostMapping("/email-certification")
    fun emailCertification(@RequestBody @Valid requestBody: EmailCertificationRequestDto): ResponseEntity<out EmailCertificationResponseDto> {
        val response = userService.emailCertification(requestBody)
        log.info("[emailCertification]: {id: ${requestBody.email}, email: ${requestBody.email}}")
        return response
    }

    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드 일치하는지 검사하는 API")
    @PostMapping("/check-certification")
    fun checkCertification(@RequestBody @Valid requestBody: CheckCertificationRequestDto): ResponseEntity<out CheckCertificationResponseDto> {
        val response = userService.checkCertification(requestBody)
        log.info("[checkCertification]: {id: ${requestBody.email}, email: ${requestBody.email}, certificationNumber: ${requestBody.certificationNumber}}")
        return response
    }

    @Operation(summary = "회원가입", description = "회원가입 하는 API")
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid requestBody: SignupRequestDto): ResponseEntity<out SignupResponseDto> {
        val response = userService.signUp(requestBody)
        log.info("[signUp]: {id: ${requestBody.email}, password: ${requestBody.password}, email: ${requestBody.email}, certificationNumber: ${requestBody.certificationNumber}}")
        return response
    }

    @Operation(summary = "로그인", description = "로그인 하는 API")
    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid requestBody: SigninRequestDto): ResponseEntity<out SigninResponseDto> {
        val response = userService.signIn(requestBody)
        log.info("[signIn]: {id: ${requestBody.email}, password: ${requestBody.password}}")
        return response
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴하는 API")
    @PostMapping("/delete-user")
    fun deleteUser(@RequestBody @Valid requestBody: DeleteIdRequestDto): ResponseEntity<out DeleteIdResponseDto> {
        val response = userService.deleteId(requestBody)
        log.info("[deleteUser]: {id: ${requestBody.email}, password: ${requestBody.password}}")
        return response
    }

    @Operation(summary = "구독회원 설정", description = "구독회원을 설정하는 API")
    @PostMapping("/register-subscription")
    fun registerSubscription(@RequestBody @Valid requestBody: RegisterSubscribeRequestDto): ResponseEntity<out RegisterSubscribeResponseDto> {
        val response = userService.registerSubscribe(requestBody)
        log.info("[registerSubscription]: {id: ${requestBody.userId}, subscribe: ${requestBody.subscribeType}}")
        return response
    }
}