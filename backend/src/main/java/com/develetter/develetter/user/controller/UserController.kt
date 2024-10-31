package com.develetter.develetter.user.controller

import com.develetter.develetter.user.global.dto.request.*
import com.develetter.develetter.user.global.dto.response.*
import com.develetter.develetter.user.service.UserService
import com.example.demo.user.global.dto.response.CheckCertificationResponseDto
import com.example.demo.user.global.dto.response.DeleteIdResponseDto
import com.example.demo.user.global.dto.response.EmailCertificationResponseDto
import com.example.demo.user.global.dto.response.IdCheckResponseDto
import com.example.demo.user.global.dto.response.RegisterSubscribeResponseDto
import com.example.demo.user.global.dto.response.SigninResponseDto
import com.example.demo.user.global.dto.response.SignupResponseDto
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.slf4j.LoggerFactory

@RestController
@RequestMapping("/api/auth")
class UserController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    /**
     * ID 중복체크(id post 요청)
     */
    @Operation(summary = "ID 중복 체크", description = "ID 중복 체크하는 API")
    @PostMapping("/id-check")
    fun idCheck(@RequestBody @Valid requestBody: IdCheckRequestDto): ResponseEntity<in IdCheckResponseDto>? {
        val response = userService.idCheck(requestBody)
        log.info("[idCheck]: {id: ${requestBody.email}}")
        return response
    }

    /**
     * 이메일 인증코드 발송(id, email을 post요청)
     */
    @Operation(summary = "이메일 인증코드 발송", description = "이메일 인증코드 발송하는 API")
    @PostMapping("/email-certification")
    fun emailCertification(@RequestBody @Valid requestBody: EmailCertificationRequestDto): ResponseEntity<in EmailCertificationResponseDto>? {
        val response = userService.emailCertification(requestBody)
        log.info("[emailCertification]: {id: ${requestBody.email}, email: ${requestBody.email}}")
        return response
    }

    /**
     * 이메일 인증번호 검사(id, email, certificationCode post 요청)
     */
    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드 일치하는지 검사하는 API")
    @PostMapping("/check-certification")
    fun checkCertification(@RequestBody @Valid requestBody: CheckCertificationRequestDto): ResponseEntity<in CheckCertificationResponseDto>? {
        val response = userService.checkCertification(requestBody)
        log.info("[checkCertification]: {id: ${requestBody.email}, email: ${requestBody.email}, certificationNumber: ${requestBody.certificationNumber}}")
        return response
    }

    /**
     * 회원가입하기 (id, password, email, certificationNumber post 요청)
     */
    @Operation(summary = "회원가입", description = "회원가입 하는 API")
    @PostMapping("/sign-up")
    fun signUp(@RequestBody @Valid requestBody: SignupRequestDto): ResponseEntity<in SignupResponseDto>? {
        val response = userService.signUp(requestBody)
        log.info("[signUp]: {id: ${requestBody.email}, password: ${requestBody.password}, email: ${requestBody.email}, certificationNumber: ${requestBody.certificationNumber}}")
        return response
    }

    /**
     * 로그인하기 (id, password post 요청)
     */
    @Operation(summary = "로그인", description = "로그인 하는 API")
    @PostMapping("/sign-in")
    fun signIn(@RequestBody @Valid requestBody: SigninRequestDto): ResponseEntity<in SigninResponseDto>? {
        val response = userService.signIn(requestBody)
        log.info("[signIn]: {id: ${requestBody.email}, password: ${requestBody.password}}")
        return response
    }

    /**
     * 계정삭제하기 (id, password post 요청 -> deletemapping으로 변경 가능)
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴하는 API")
    @PostMapping("/delete-user")
    fun deleteUser(@RequestBody @Valid requestBody: DeleteIdRequestDto): ResponseEntity<in DeleteIdResponseDto>? {
        val response = userService.deleteId(requestBody)
        log.info("[deleteUser]: {id: ${requestBody.email}, password: ${requestBody.password}}")
        return response
    }

    /**
     * 구독회원 설정 (id와 subscribeType을 post 요청)
     */
    @Operation(summary = "구독회원 설정", description = "구독회원을 설정하는 API")
    @PostMapping("/register-subscription")
    fun registerSubscription(@RequestBody @Valid requestBody: RegisterSubscribeRequestDto): ResponseEntity<in RegisterSubscribeResponseDto>? {
        val response = userService.registerSubscribe(requestBody)
        log.info("[registerSubscription]: {id: ${requestBody.userId}, subscribe: ${requestBody.subscribeType}}")
        return response
    }
}