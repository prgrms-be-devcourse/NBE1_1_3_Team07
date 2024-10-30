package com.develetter.develetter.mail.config;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Component;

@Component
public class JobParametersFactory {
    // JobParameters를 생성하는 메서드
    public JobParameters createJobParameters() {
        return new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis()) // runId가 null이면 현재 시간 사용
                .toJobParameters();
    }
}
