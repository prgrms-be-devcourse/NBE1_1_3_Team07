package com.develetter.develetter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableJpaAuditing // 등록시각, 수정시각을 위한 전체 auditing 활성화를 위한 애노테이션
@EnableScheduling // 스케줄링 활성화
@SpringBootApplication
class DeveletterApplication

fun main(args: Array<String>) {
    runApplication<DeveletterApplication>(*args)
}
