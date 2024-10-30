package com.develetter.develetter.userfilter.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.user.provider.JwtProvider;
import com.develetter.develetter.userfilter.dto.UserFilterReqDto;
import com.develetter.develetter.userfilter.service.UserFilterService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userfilter")
@RequiredArgsConstructor
@Slf4j
public class UserFilterController {

    private final UserFilterService userFilterService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "사용자 필터 등록", description = "사용자 필터 등록하는 API")
    @PostMapping()
    public ApiResponseDto<Void> registerUserFilter(@RequestBody UserFilterReqDto userFilterReqDto,
                                                   @RequestHeader("Authorization") String authorizationHeader) {

//        Long userId = Long.valueOf(jwtProvider.validate(authorizationHeader)); TODO: validate가 email이 아닌 userId를 반환하도록 해야함.
        Long userId = 1L;

        userFilterService.registerUserFilter(userId, userFilterReqDto);

        return new ApiResponseDto<>(200, "유저 필터 등록 완료");
    }
}
