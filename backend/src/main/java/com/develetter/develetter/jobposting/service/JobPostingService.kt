package com.develetter.develetter.jobposting.service

import com.develetter.develetter.jobposting.dto.JobPostingEmailDto
import com.develetter.develetter.jobposting.dto.JobSearchResDto

interface JobPostingService {

    fun searchJobs(startIdx: Int, sevenDaysAgo: String): JobSearchResDto

    fun getFilteredJobPostingsByUserId(userId: Long): List<JobPostingEmailDto>?
}
