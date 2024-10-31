package com.develetter.develetter.mail.scheduler

import com.develetter.develetter.mail.config.JobParametersFactory
import mu.KotlinLogging
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class MailSchedulerImpl(
    private val jobLauncher: JobLauncher,
    private val jobRegistry: JobRegistry,
    private val jobParametersFactory: JobParametersFactory
) : MailScheduler {

    // 월요일 오전 9시마다
    @Scheduled(cron = "0 0 9 * * MON")
    //@Scheduled(cron = "20 * * * * *")
    override fun sendingMails() {
        try {
            // 메일 전송
            val jobParameters = jobParametersFactory.createJobParameters()

            jobLauncher.run(jobRegistry.getJob("mailJob"), jobParameters)
        } catch (e: Exception) {
            log.error("Scheduled Mail Sent Error", e)
        }
    }
}
