package com.develetter.develetter.user.controller;

import com.develetter.develetter.user.global.dto.request.*;
import com.develetter.develetter.user.global.dto.response.*;
import com.develetter.develetter.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    /**
     * ID 중복체크(id post 요청)
     * @param requestBody
     * @return
     */
    @Operation(summary = "ID 중복 체크", description = "ID 중복 체크하는 API")
    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(@RequestBody @Valid IdCheckRequestDto requestBody) {
        ResponseEntity<? super IdCheckResponseDto> response= userService.idCheck(requestBody);
        log.info("[idCheck]: {id:" + requestBody.getEmail()+ "}");
        return response;
    }

    /**
     * 이메일 인증코드 발송(id, email을 post요청)
     * @param requestBody
     * @return
     */
    @Operation(summary = "이메일 인증코드 발송", description = "이메일 인증코드 발송하는 API")
    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        ResponseEntity<? super EmailCertificationResponseDto> response= userService.emailCertification(requestBody);
        log.info("[emailCertification]: {id: " + requestBody.getEmail() + ", email: " + requestBody.getEmail() + "}");
        return response;
    }

    /**
     * 이메일 인증번호 검사(id, email, certificationCode post 요청)
     * @param requestBody
     * @return
     */
    @Operation(summary = "이메일 인증코드 검사", description = "이메일 인증코드 일치하는지 검사하는 API")
    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(@RequestBody @Valid CheckCertificationRequestDto requestBody) {
        ResponseEntity<? super CheckCertificationResponseDto> response= userService.checkCertification(requestBody);
        log.info("[checkCertification]: {id: " + requestBody.getEmail() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
        return response;
    }

    /**
     * 회원가입하기 (id, password, email, certificationNumber post 요청)
     * @param requestBody
     * @return
     */
    @Operation(summary = "회원가입", description = "회원가입 하는 API")
    @PostMapping("/sign-up")
    public ResponseEntity<? super SignupResponseDto> signUp(@RequestBody @Valid SignupRequestDto requestBody) {
        ResponseEntity<? super SignupResponseDto> response= userService.signUp(requestBody);
        log.info("[signUp]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
        return response;
    }

    /**
     * 로그인하기 (id, password post 요청)
     * @param requestBody
     * @return
     */
    @Operation(summary = "로그인", description = "로그인 하는 API")
    @PostMapping("/sign-in")
    public ResponseEntity<? super SigninResponseDto> signIn(@RequestBody @Valid SigninRequestDto requestBody) {
        ResponseEntity<? super SigninResponseDto> response= userService.signIn(requestBody);
        log.info("[signIn]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    /**
     * 계정삭제하기 (id, password post 요청 -> deletemapping으로 변경 가능)
     * @param requestBody
     * @return
     */
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴하는 API")
    @PostMapping("/delete-user")
    public ResponseEntity<? super DeleteIdResponseDto> deleteUser(@RequestBody @Valid DeleteIdRequestDto requestBody) {
        ResponseEntity<? super DeleteIdResponseDto> response= userService.deleteId(requestBody);
        log.info("[deleteUser]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }
    @Operation(summary = "구독회원 설", description = "구독회원을 설정하는 API")
    @PostMapping("/register-subscription")
    public ResponseEntity<? super RegisterSubscribeResponseDto> registerSubscription(@RequestBody @Valid RegisterSubscribeRequestDto requestBody) {
        ResponseEntity<? super RegisterSubscribeResponseDto> response = userService.registerSubscribe(requestBody);
        log.info("[registerSubscription]: {id: " + requestBody.getUserId() + ",  subscribe: " + requestBody.getSubscribeType() + "}");
        return response;
    }
}
