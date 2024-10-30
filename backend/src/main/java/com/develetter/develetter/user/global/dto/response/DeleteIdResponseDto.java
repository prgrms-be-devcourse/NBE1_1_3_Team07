package com.develetter.develetter.user.global.dto.response;

import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import com.develetter.develetter.user.global.dto.LogInResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteIdResponseDto extends LogInResponseDto {

    private DeleteIdResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> idNotFound(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<LogInResponseDto> idNotMatching(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.ID_NOT_MATCHING, ResponseMessage.ID_NOT_MATCHING);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }

}
