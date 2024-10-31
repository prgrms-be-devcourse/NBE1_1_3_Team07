package com.develetter.develetter.mail.batch

import com.develetter.develetter.mail.entity.Mail
import com.develetter.develetter.mail.repository.MailRepository
import com.develetter.develetter.mail.service.AsyncMailService
import com.develetter.develetter.mail.service.ConferenceCalendarService
import com.develetter.develetter.mail.service.MailService
import com.develetter.develetter.user.global.entity.UserEntity
import com.develetter.develetter.user.repository.UserRepository
import mu.KotlinLogging
import org.springframework.batch.core.*
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager

@Configuration
open class MailBatch(
    private val jobRepository: JobRepository,
    private val mailRepository: MailRepository,
    private val userRepository: UserRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val conferenceCalendarService: ConferenceCalendarService,
    private val asyncMailService: AsyncMailService,
    private val mailService: MailService
) {
    private companion object {
        const val CHUNK_SIZE = 5
    }

    @Bean
    open fun mailJob(): Job {
        return JobBuilder("mailJob", jobRepository)
            .start(saveMailStep())
            .next(sendMailStep())
            .build()
    }

    //메일 내용 저장
    @Bean
    open fun saveMailStep(): Step {
        return StepBuilder("saveMailStep", jobRepository)
            .chunk<UserEntity, Mail>(CHUNK_SIZE, platformTransactionManager)
            .reader(userReader())
            .processor(saveMailProcessor())
            .writer(mailWriter())
            .build()
    }

    @Bean
    open fun userReader(): RepositoryItemReader<UserEntity> {
        return RepositoryItemReaderBuilder<UserEntity>()
            .name("userReader")
            .pageSize(CHUNK_SIZE)
            .methodName("findAll")
            .repository(userRepository)
            .sorts(getSortMap())
            .build()
    }

    @Bean
    open fun saveMailProcessor(): ItemProcessor<UserEntity, Mail> {
        return ItemProcessor { user -> Mail(user.id) }
    }

    @Bean
    open fun mailWriter(): RepositoryItemWriter<Mail> {
        return RepositoryItemWriterBuilder<Mail>()
            .repository(mailRepository)
            .methodName("save")
            .build()
    }

    //메일 전송
    @Bean
    open fun sendMailStep(): Step {
        return StepBuilder("sendMailStep", jobRepository)
            .chunk<Mail, Mail>(CHUNK_SIZE, platformTransactionManager)
            .reader(mailReader())
            .processor(sendMailProcessor())
            .writer(emptyMailWriter())
            .build()
    }

    @Bean
    open fun mailReader(): RepositoryItemReader<Mail> {
        return RepositoryItemReaderBuilder<Mail>()
            .name("mailReader")
            .pageSize(CHUNK_SIZE)
            .repository(mailRepository)
            .methodName("findByDeletedIsFalse")
            .sorts(getSortMap())
            .build()
    }

    @Bean
    open fun sendMailProcessor(): ItemProcessor<Mail, Mail> {
        val conferenceHtml = conferenceCalendarService.createConferenceCalendar()

        return ItemProcessor { mail ->
            asyncMailService.sendMail(mail, conferenceHtml.toString())
            mailService.updateMailDeleted(mail.id!!)

            mail
        }
    }

    @Bean
    open fun emptyMailWriter(): ItemWriter<Mail> {
        return ItemWriter { /* 빈 구현: 아무 동작도 수행하지 않음 */ }
    }

    private fun getSortMap(): Map<String, Sort.Direction> {
        return mapOf("id" to Sort.Direction.ASC)
    }
}
