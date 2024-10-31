package com.develetter.develetter.jobposting.repository

import com.develetter.develetter.jobposting.entity.FilteredJobPosting
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface FilteredJobPostingRepository : JpaRepository<FilteredJobPosting, Long> {
    fun findByUserId(userId: Long): Optional<FilteredJobPosting>
}
