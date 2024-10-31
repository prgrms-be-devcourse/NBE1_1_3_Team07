package com.develetter.develetter.jobposting.service

import com.develetter.develetter.global.util.DtoUtil
import com.develetter.develetter.jobposting.converter.Converter
import com.develetter.develetter.jobposting.dto.JobPostingEmailDto
import com.develetter.develetter.jobposting.dto.JobSearchParams
import com.develetter.develetter.jobposting.dto.JobSearchResDto
import com.develetter.develetter.jobposting.entity.FilteredJobPosting
import com.develetter.develetter.jobposting.entity.JobPosting
import com.develetter.develetter.jobposting.exception.JobSearchException
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository
import com.develetter.develetter.jobposting.repository.JobPostingRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class JobPostingServiceImpl(
    @Value("\${api.saramin.accesskey}")
    private val accessKey: String,
    @Value("\${api.saramin.baseurl}")
    private val baseUrl: String,
    private val webClient: WebClient,
    private val jobPostingRepository: JobPostingRepository,
    private val filteredJobPostingRepository: FilteredJobPostingRepository
) : JobPostingService {

    override fun searchJobs(startPage: Int, sevenDaysAgo: String): JobSearchResDto {
        try {
            val params = JobSearchParams.defaultParams(startPage, sevenDaysAgo)

            val jobSearchResDto = webClient.get()
                .uri { uriBuilder ->
                    uriBuilder
                        .scheme("https")
                        .host(baseUrl)
                        .path("/job-search")
                        .queryParam("access-key", accessKey)
                    params.forEach { (key, value) ->
                        value?.let {
                            uriBuilder.queryParam(key, it)
                        }
                    }
                    val uri = uriBuilder.build()
                    log.info("Constructed URI: $uri") // URI 로그 출력
                    uri // URI 반환
                }

                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JobSearchResDto::class.java)
                .block() // 동기 처리

            if (jobSearchResDto == null || jobSearchResDto.jobs.job == null) {
                log.error("사람인 채용정보 API 응답이 올바르지 않습니다: 채용공고를 제공받지 못했습니다.")
                throw JobSearchException("사람인 API 응답이 올바르지 않습니다.")
            }

            val jobPostings = jobSearchResDto.jobs.job
                .map(Converter::toEntity)
                .filter {
                    it.experienceMin in 0..2 && it.experienceMax in 0..3
                }

            jobPostingRepository.saveAll(jobPostings)
            log.info("success [searchJobs] page: $startPage")

            return jobSearchResDto

        } catch (e: Exception) {
            log.error("Error occurred during [searchJobs]", e)
            throw e
        }
    }

    override fun getFilteredJobPostingsByUserId(userId: Long): List<JobPostingEmailDto>? {
        val filteredJobPosting = filteredJobPostingRepository.findByUserId(userId).orElse(null)
            ?: return null

        if (filteredJobPosting.jobPostings.isEmpty()) {
            return null
        }

        val jobPostingIds = filteredJobPosting.jobPostings.split(",")
            .map { it.trim().toLong() }
            .take(5)

        val jobPostings = jobPostingRepository.findAllById(jobPostingIds)

        return jobPostings.map(Converter::toEmailDTO)
    }

    companion object {
        private val log = LoggerFactory.getLogger(JobPostingServiceImpl::class.java)
    }
}
