package com.develetter.develetter.jobposting.repository

import com.develetter.develetter.jobposting.entity.JobPosting
import org.springframework.data.jpa.repository.JpaRepository

interface JobPostingRepository : JpaRepository<JobPosting, Long>
