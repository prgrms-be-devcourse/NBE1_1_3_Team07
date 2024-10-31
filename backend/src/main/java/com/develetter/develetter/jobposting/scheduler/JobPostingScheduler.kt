package com.develetter.develetter.jobposting.scheduler

import org.springframework.beans.factory.annotation.Autowired

interface JobPostingScheduler {
    fun fetchJobPostings()
}