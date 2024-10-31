package com.develetter.develetter.mail.config

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.stereotype.Component

@Component
class JobParametersFactory {
    // JobParameters를 생성하는 메서드
    fun createJobParameters(): JobParameters {
        return JobParametersBuilder()
            .addLong("run.id", System.currentTimeMillis())
            .toJobParameters()
    }
}
