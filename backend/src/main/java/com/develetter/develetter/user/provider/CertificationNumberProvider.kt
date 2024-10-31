package com.develetter.develetter.user.provider;

import java.util.UUID;

public class CertificationNumberProvider {

    public static String generateNumber() {
        // UUID 생성 후 하이픈을 제거한 문자열로 변환
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // UUID에서 영문자와 숫자로만 이루어진 6자리 문자열 추출
        String number = uuid.substring(0, 6);

        return number;
    }
}
