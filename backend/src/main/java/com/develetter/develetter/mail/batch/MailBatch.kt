package com.develetter.develetter.mail.batch

import com.develetter.develetter.mail.entity.Mail
import com.develetter.develetter.mail.repository.MailRepository
import com.develetter.develetter.mail.service.AsyncMailService
import com.develetter.develetter.mail.service.ConferenceCalendarService
import com.develetter.develetter.mail.service.MailService
import com.develetter.develetter.user.global.entity.UserEntity
import com.develetter.develetter.user.repository.UserRepository
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.springframework.batch.core.*
import org.springframework.batch.core.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.RepositoryItemWriter
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager

private val log = KotlinLogging.logger {}

@Configuration
open class MailBatch(
    private val jobRepository: JobRepository,
    private val mailRepository: MailRepository,
    private val userRepository: UserRepository,
    private val platformTransactionManager: PlatformTransactionManager,
    private val conferenceCalendarService: ConferenceCalendarService,
    private val asyncMailService: AsyncMailService,
    private val mailService: MailService
) : CoroutineScope by CoroutineScope(Dispatchers.IO)  {
    private companion object {
        const val CHUNK_SIZE = 10
        //각 파티션에서 처리할 메일 개수
        const val PARTITION_SIZE = 10
        //한 번에 처리할 파티션 수
        const val GRID_SIZE = 2
    }

    @Bean
    open fun mailJob(): Job {
        return JobBuilder("mailJob", jobRepository)
            .start(saveMailStep())
            .next(partitionStep())
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

    // 파티션 스텝
    @Bean
    open fun partitionStep(): Step {
        return StepBuilder("partitionStep", jobRepository)
            .partitioner("sendMailStep", mailPartitioner())
            .step(sendMailStep())
            .gridSize(GRID_SIZE)
            .build()
    }

    // 메일 파티셔너
    @Bean
    open fun mailPartitioner(): Partitioner {
        return Partitioner { partitionStepExecution ->
            val result = mutableMapOf<String, ExecutionContext>()
            val totalMails = mailRepository.countByDeletedIsFalse()

            //겹치지 않게 read 하기 위해
            for (i in 0 until PARTITION_SIZE) {
                val context = ExecutionContext()
                val startId = (i * PARTITION_SIZE) + 1
                context.putInt("startId", startId)
                context.putInt("endId", minOf(startId + PARTITION_SIZE - 1, totalMails))
                result["partition$i"] = context
            }
            result
        }
    }


    //메일 전송
    @Bean
    open fun sendMailStep(): Step {
        val customMailReader = CustomMailReader(mailRepository)

        return StepBuilder("sendMailStep", jobRepository)
            .chunk<Mail, Mail>(CHUNK_SIZE, platformTransactionManager)
            .reader(customMailReader)
            .processor(sendMailProcessor())
            .writer(emptyMailWriter())
            .listener(object : StepExecutionListener {
                override fun beforeStep(stepExecution: StepExecution) {
                    // ExecutionContext에서 startId와 endId를 가져와 커스텀 리더에 설정
                    val startId = stepExecution.executionContext.getInt("startId").toLong()
                    val endId = stepExecution.executionContext.getInt("endId").toLong()
                    customMailReader.setRange(startId, endId) // 범위를 설정
                }

                override fun afterStep(stepExecution: StepExecution): ExitStatus {
                    return stepExecution.exitStatus
                }
            })
            .build()
    }


    @Bean
    open fun mailReader(): CustomMailReader {
        return CustomMailReader(mailRepository) // CustomMailReader 사용
    }

    // mailPartitioner()에서 설정한 startId와 endId를 CustomMailReader에 설정
    class CustomMailReader(private val mailRepository: MailRepository) : ItemReader<Mail> {
        private var startId: Long = 0
        private var endId: Long = 0
        private var currentIndex: Long = startId

        fun setRange(startId: Long, endId: Long) {
            this.startId = startId
            this.endId = endId
            this.currentIndex = startId
        }

        override fun read(): Mail? {
            if (currentIndex > endId) return null

            // 현재 인덱스에서 메일 목록을 가져오고 currentIndex 증가
            val mailList = mailRepository.findByDeletedIsFalseAndIdBetween(currentIndex, endId)

            return if (mailList.isNotEmpty()) {
                val mail = mailList.first() // 첫 번째 메일 반환
                currentIndex++ // 현재 인덱스 증가
                mail
            } else {
                null // 메일이 없으면 null 반환
            }
        }
    }


    @Bean
    open fun sendMailProcessor(): ItemProcessor<Mail, Mail> {
        val conferenceHtml = conferenceCalendarService.createConferenceCalendar()

        return ItemProcessor { mail ->
            launch {
                try {
                    asyncMailService.sendMail(mail, conferenceHtml.toString())
                    mailService.updateMailDeleted(mail.id)
                } catch (_: Exception) {
                }
            }
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


