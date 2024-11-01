package com.develetter.develetter.mail.batch;

import com.develetter.develetter.mail.entity.Mail;
import com.develetter.develetter.mail.repository.MailRepository;
import com.develetter.develetter.mail.service.AsyncMailService;
import com.develetter.develetter.mail.service.ConferenceCalendarService;
import com.develetter.develetter.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MailBatch {
    private final JobRepository jobRepository;
    private final MailRepository mailRepository;
    private final UserRepository userRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final ConferenceCalendarService conferenceCalendarService;
    private final AsyncMailService asyncMailService;
    private final MailService mailService;


    private static final int CHUNK_SIZE = 5;

    @Bean
    public Job mailJob() {
        return new JobBuilder("mailJob", jobRepository)
                .start(saveMailStep())
                .next(sendMailStep())
                .build();
    }

    //메일 내용 저장
    @Bean
    public Step saveMailStep() {
        return new StepBuilder("saveMailStep", jobRepository)
                .<UserEntity, Mail> chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(userReader())
                .processor(saveMailProcessor())
                .writer(mailWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<UserEntity> userReader() {
        return new RepositoryItemReaderBuilder<UserEntity>()
                .name("userReader")
                .pageSize(CHUNK_SIZE)
                .methodName("findAll")
                .repository(userRepository)
                .sorts((getSortMap()))
                .build();
    }

    @Bean
    public ItemProcessor<UserEntity, Mail> saveMailProcessor() {
        return user -> new Mail(user.getId());
    }

    @Bean
    public RepositoryItemWriter<Mail> mailWriter() {
        return new RepositoryItemWriterBuilder<Mail>()
                .repository(mailRepository)
                .methodName("save")
                .build();
    }

    //메일 전송
    @Bean
    public Step sendMailStep() {
        return new StepBuilder("sendMailStep", jobRepository)
                .<Mail, Mail> chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(mailReader())
                .processor(sendMailProcessor())
                .writer(emptyMailWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<Mail> mailReader() {
        return new RepositoryItemReaderBuilder<Mail>()
                .name("mailReader")
                .pageSize(CHUNK_SIZE)
                .repository(mailRepository)
                .methodName("findByDeletedIsFalse")
                .sorts(getSortMap())
                .build();
    }

    @Bean
    public ItemProcessor<Mail, Mail> sendMailProcessor() {
        String conferenceHtml = conferenceCalendarService.createConferenceCalendar();

        return mail -> {
            try {
                asyncMailService.sendMail(mail, conferenceHtml);
            } catch (Exception e) {
                //전송 실패시 재전송
                log.error("Send Failed Mail for User ID: " + mail.getId());
                asyncMailService.sendMail(mail, conferenceHtml);
            } finally {
                mailService.updateMailDeleted(mail.getId());
            }
            return mail;
        };
    }

    @Bean
    public ItemWriter<Mail> emptyMailWriter() {
        return items -> {
            // 빈 구현: 아무 동작도 수행하지 않음
        };
    }

    private Map<String, Sort.Direction> getSortMap() {
        return Map.of("id", Sort.Direction.ASC);
    }

}
