package com.develetter.develetter.user.global.dto.response;

import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import com.develetter.develetter.user.global.dto.LogInResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignupResponseDto extends LogInResponseDto {

    private String role;

    private SignupResponseDto(String role) {
        super();
        this.role = role;
    }

    public static ResponseEntity<LogInResponseDto> duplicateId(){
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> certificationFail(){
        LogInResponseDto responseBody=new LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> wrongRole() {
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.WRONG_ROLE, ResponseMessage.WRONG_ROLE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}
