package com.develetter.develetter.mail.scheduler;

import com.develetter.develetter.mail.config.JobParametersFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailSchedulerImpl implements MailScheduler {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobParametersFactory jobParametersFactory;

    @Override
    // 월요일 오전 9시마다
    @Scheduled(cron = "0 0 9 * * MON")
    public void sendingMails() {
        try {
            //메일 전송
            JobParameters jobParameters = jobParametersFactory.createJobParameters();

            jobLauncher.run(jobRegistry.getJob("mailJob"), jobParameters);

        } catch (Exception e) {
            log.error("Scheduled Mail Sent Error", e);
        }
    }
}
