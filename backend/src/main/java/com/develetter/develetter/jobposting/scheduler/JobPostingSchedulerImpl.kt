package com.develetter.develetter.jobposting.scheduler

import com.develetter.develetter.jobposting.dto.JobSearchResDto
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository
import com.develetter.develetter.jobposting.service.JobPostingService
import com.develetter.develetter.userfilter.entity.UserFilter
import com.develetter.develetter.userfilter.repository.UserFilterRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class JobPostingSchedulerImpl(
    @Value("\${api.saramin.baseurl}")
    private val apiURL: String,
    private val jobPostingService: JobPostingService,
    private val userFilterRepository: UserFilterRepository,
    private val filteredJobPostingRepository: FilteredJobPostingRepository,
    private val jobLauncher: JobLauncher,
    private val filterJobPostingsJob: Job
) : JobPostingScheduler {

    @Scheduled(cron = "0 52 16 * * *") // 매일 00:00시 정각에 실행
    @Transactional
    override fun fetchJobPostings() {
        var startPage = 0

        val twoWeeksAgo = LocalDateTime.now()
            .minusDays(14)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        while (true) {
            val jobSearchResDto: JobSearchResDto = jobPostingService.searchJobs(startPage++, twoWeeksAgo)

            if (jobSearchResDto.jobs.count < 100) break
        }
    }

    @Scheduled(cron = "20 52 16 * * *") // 매 30초마다 실행 // TODO 추후에 수정 예정
    fun runBatchForAllUsers() {
        // 모든 필터링된 잡 포스팅 삭제
        filteredJobPostingRepository.deleteAll()

        val userFilters: List<UserFilter> = userFilterRepository.findAll() // 모든 UserFilter 조회
        val userIds: List<Long> = userFilters.mapNotNull { it.userId } // userId가 null이 아닌 경우만 사용

        if (userIds.isEmpty()) {
            println("No user filters found to run batch.")
            return
        }

        // JobParameters 설정
        val jobParameters = JobParametersBuilder()
            .addString("userIds", userIds.joinToString(",")) // 모든 userId를 문자열로 변환
            .addString("time", System.currentTimeMillis().toString()) // 현재 시간 추가
            .toJobParameters()

        // Job 실행
        try {
            jobLauncher.run(filterJobPostingsJob, jobParameters)
            println("Job started for userIds: $userIds")
        } catch (e: Exception) {
            System.err.println("Failed to run job due to: \${e.message}")
        }
    }
}
