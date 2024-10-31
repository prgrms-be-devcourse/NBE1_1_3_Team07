package com.develetter.develetter.jobposting.batch

import com.develetter.develetter.jobposting.service.FilteredJobPostingCacheService
import com.develetter.develetter.userfilter.entity.JobPostingKeyword
import com.develetter.develetter.userfilter.entity.UserFilter
import com.develetter.develetter.userfilter.repository.UserFilterRepository
import com.develetter.develetter.jobposting.entity.FilteredJobPosting
import com.develetter.develetter.jobposting.entity.JobPosting
import com.develetter.develetter.jobposting.entity.QJobPosting
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import java.util.*
import java.util.stream.Collectors

@Configuration
@RequiredArgsConstructor
@Slf4j
class JobPostingBatch(
    private val platformTransactionManager: PlatformTransactionManager,
    private val jobRepository: JobRepository,
    private val filteredJobPostingRepository: FilteredJobPostingRepository,
    private val userFilterRepository: UserFilterRepository,
    private val queryFactory: JPAQueryFactory,
    private val emf: EntityManagerFactory,
    @Autowired private val filteredJobPostingCacheService: FilteredJobPostingCacheService
) {

    private val chunkSize = 100

    @Bean
    fun jobExecutionListener(): JobExecutionListener {
        return object : JobExecutionListener {
            override fun beforeJob(jobExecution: JobExecution) {
                println("Job 시작: " + jobExecution.jobInstance.jobName)
            }

            override fun afterJob(jobExecution: JobExecution) {
                if (jobExecution.status == BatchStatus.COMPLETED) {
                    println("Job 완료: " + jobExecution.jobInstance.jobName)
                    saveAllCachedJobPostingsToDB(jobExecution)
                } else {
                    System.err.println("Job 실패: " + jobExecution.allFailureExceptions)
                }
            }

            private fun saveAllCachedJobPostingsToDB(jobExecution: JobExecution) {
                val userIdsParam = jobExecution.jobParameters.getString("userIds")

                if (userIdsParam != null) {
                    val userIds = userIdsParam.split(",").map { it.toLong() }

                    for (userId in userIds) {
                        val jobPostingIds = filteredJobPostingCacheService.getJobPostingsFromCache(userId)
                        val jobPostings = jobPostingIds?.joinToString(",") { it.toString() } ?: ""
                        val filteredJobPosting = FilteredJobPosting(
                            userId = userId,
                            jobPostings = jobPostings
                        )
                        filteredJobPostingRepository.save(filteredJobPosting)

                        filteredJobPostingCacheService.clearCache(userId)
                    }
                }
            }
        }
    }


    @Bean
    fun filterJobPostingsJob(jpaQueryFactory: JPAQueryFactory): Job {
        return JobBuilder("filterJobPostingsJob", jobRepository)
            .listener(jobExecutionListener())
            .start(filterJobPostingsStep(jpaQueryFactory))
            .incrementer(RunIdIncrementer())
            .build()
    }

    @Bean
    fun filterJobPostingsStep(jpaQueryFactory: JPAQueryFactory): Step {
        return StepBuilder("filterJobPostingsStep", jobRepository)
            .chunk<JobPosting, Long>(chunkSize, platformTransactionManager)
            .reader(reader())
            .processor(itemProcessor(""))
            .writer(customItemWriter(""))
            .build()
    }

    @Bean
    fun reader(): QuerydslPagingItemReader<JobPosting> {
        return QuerydslPagingItemReader(emf, chunkSize) { queryFactory ->
            queryFactory.selectFrom(QJobPosting.jobPosting)
        }
    }

    @Bean
    @StepScope
    fun itemProcessor(@Value("#{jobParameters['userIds']}") userIds: String): ItemProcessor<JobPosting, Long> {
        val userIdList = userIds.split(",").map { it.toLong() }

        val userFilters = userFilterRepository.findAll()
        val userFilterMap = userFilters.associateBy({ it.userId }, { it.jobpostingKeywords })

        return ItemProcessor { jobPosting ->
            for (userId in userIdList) {
                val jobpostingKeywords = userFilterMap[userId]

                if (jobpostingKeywords != null &&
                    containsIgnoreCase(jobPosting.jobName, parseKeywords(jobpostingKeywords.jobNames)) &&
                    containsIgnoreCase(jobPosting.locationName, parseKeywords(jobpostingKeywords.locationNames)) &&
                    containsIgnoreCase(jobPosting.jobTypeName, parseKeywords(jobpostingKeywords.jobTypeNames)) &&
                    containsIgnoreCase(jobPosting.industryName, parseKeywords(jobpostingKeywords.industryNames)) &&
                    containsIgnoreCase(
                        jobPosting.educationLevelName,
                        parseKeywords(jobpostingKeywords.educationLevelNames)
                    )
                ) {
                    return@ItemProcessor jobPosting.id
                }
            }
            null
        }
    }

    @Bean
    @StepScope
    fun customItemWriter(@Value("#{jobParameters['userIds']}") userIds: String): ItemWriter<Long> {
        val userIdList = userIds.split(",").map { it.toLong() }

        return ItemWriter { items ->
            for (jobPostingId in items) {
                for (userId in userIdList) {
                    filteredJobPostingCacheService.addJobPostingToCache(userId, jobPostingId)
                }
            }
        }
    }

    private fun parseKeywords(keywords: String?): List<String> {
        if (keywords.isNullOrBlank()) {
            return emptyList()
        }
        return keywords.split(",").map { it.trim().lowercase() }.filter { it.isNotEmpty() }
    }

    private fun containsIgnoreCase(fieldValue: String?, keywords: List<String>): Boolean {
        if (fieldValue == null) {
            return false
        }
        return keywords.any { fieldValue.lowercase().contains(it) }
    }
}
