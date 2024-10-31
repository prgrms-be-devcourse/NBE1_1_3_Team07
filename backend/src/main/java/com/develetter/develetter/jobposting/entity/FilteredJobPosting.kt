package com.develetter.develetter.jobposting.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor
import lombok.experimental.SuperBuilder

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "filtered_job_posting")
class FilteredJobPosting(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "job_postings", nullable = false)
    var jobPostings: String = ""

) : BaseEntity() {

    // jobPostings 필드에 새로운 JobPosting ID를 추가하는 메서드
    fun addJobPosting(jobPostingId: Long) {
        jobPostings = if (jobPostings.isEmpty()) {
            jobPostingId.toString()
        } else {
            "$jobPostings,$jobPostingId"
        }
    }
}
