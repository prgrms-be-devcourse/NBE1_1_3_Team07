package com.develetter.develetter.user.global.dto.response;


import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import com.develetter.develetter.user.global.dto.LogInResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EmailCertificationResponseDto extends LogInResponseDto {

    private EmailCertificationResponseDto(){
        super();
    }


    public static ResponseEntity<LogInResponseDto> duplicateId(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> mailSendFail(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.MAIL_FAIL, ResponseMessage.MAIL_FAIL);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
}
