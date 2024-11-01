package com.develetter.develetter.user.provider

import java.util.UUID

object CertificationNumberProvider {

    fun generateNumber(): String {
        // UUID 생성 후 하이픈을 제거한 문자열로 변환
        val uuid = UUID.randomUUID().toString().replace("-", "")

        // UUID에서 영문자와 숫자로만 이루어진 6자리 문자열 추출
        return uuid.substring(0, 6)
    }
}