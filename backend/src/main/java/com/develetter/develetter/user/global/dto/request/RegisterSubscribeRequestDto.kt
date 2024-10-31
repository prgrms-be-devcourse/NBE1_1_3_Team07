package com.develetter.develetter.user.global.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterSubscribeRequestDto{
    private Long userId;
    private String subscribeType;
}
