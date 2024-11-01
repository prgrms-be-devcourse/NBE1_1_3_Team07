package com.develetter.develetter.jobposting.converter

import com.develetter.develetter.jobposting.dto.JobPostingEmailDto
import com.develetter.develetter.jobposting.dto.JobSearchResDto.Job
import com.develetter.develetter.jobposting.entity.JobPosting
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Converter {

    // DTO -> Entity 변환 메서드
    fun toEntity(job: Job): JobPosting {
        return JobPosting(
            url = job.url,
            active = job.active == 1,
            companyName = job.company?.detail?.name,
            companyUrl = job.company?.detail?.href,
            title = job.position?.title,
            industryCode = job.position?.industry?.code,
            industryName = job.position?.industry?.name,
            locationCode = job.position?.location?.code,
            locationName = job.position?.location?.name,
            jobTypeCode = job.position?.jobType?.code,
            jobTypeName = job.position?.jobType?.name,
            jobMidCode = job.position?.jobMidCode?.code,
            jobMidName = job.position?.jobMidCode?.name,
            jobCode = job.position?.jobCode?.code,
            jobName = job.position?.jobCode?.name,
            experienceCode = job.position?.experienceLevel?.code,
            experienceMin = job.position?.experienceLevel?.min,
            experienceMax = job.position?.experienceLevel?.max,
            experienceName = job.position?.experienceLevel?.name,
            educationLevelCode = job.position?.requiredEducationLevel?.code,
            educationLevelName = job.position?.requiredEducationLevel?.name,
            keyword = job.keyword,
            salaryCode = job.salary?.code,
            salaryName = job.salary?.name,
            postingTimestamp = job.postingTimestamp,
            postingDate = parseDate(job.postingDate),
            modificationTimestamp = job.modificationTimestamp,
            openingTimestamp = job.openingTimestamp,
            expirationTimestamp = job.expirationTimestamp,
            expirationDate = parseDate(job.expirationDate),
            closeTypeCode = job.closeType?.code,
            closeTypeName = job.closeType?.name,
            readCount = job.readCnt ?: 0,
            applyCount = job.applyCnt ?: 0
        )
    }

    // 날짜 파싱 처리 메서드
    private fun parseDate(date: String?): LocalDateTime? {
        return date?.let {
            OffsetDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toLocalDateTime()
        }
    }

    // Entity -> Email DTO 변환 메서드
    fun toEmailDTO(jobPosting: JobPosting): JobPostingEmailDto {
        return JobPostingEmailDto(
            url = jobPosting.url ?: "",
            companyName = jobPosting.companyName ?: "",
            title = jobPosting.title ?: "",
            industryName = jobPosting.industryName ?: "",
            locationName = jobPosting.locationName ?: "",
            jobTypeName = jobPosting.jobTypeName ?: "",
            experienceName = jobPosting.experienceName ?: "",
            postingDate = jobPosting.postingDate ?: LocalDateTime.now(),
            expirationDate = jobPosting.expirationDate ?: LocalDateTime.now()
        )
    }
}
