package com.develetter.develetter.user.global.dto.response;

import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import com.develetter.develetter.user.global.dto.LogInResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SigninResponseDto extends LogInResponseDto {
    private String token;
    private int expirationTime;
    private String role;

    private SigninResponseDto(String token, String role) {
        super();
        this.token = token;
        this.expirationTime=3600; // 1 hour
        this.role = role;
    }

    public static ResponseEntity<SigninResponseDto> success(String token,String role) {
        SigninResponseDto responseBody = new SigninResponseDto(token, role);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> signInFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
