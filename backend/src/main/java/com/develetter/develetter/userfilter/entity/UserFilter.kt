package com.develetter.develetter.userfilter.entity

import com.develetter.develetter.global.entity.BaseEntity
import com.develetter.develetter.userfilter.dto.UserFilterReqDto
import jakarta.persistence.*

@Entity
@Table(name = "user_filter")
class UserFilter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Embedded
    var jobpostingKeywords: JobPostingKeyword? = null, // 임베디드 타입

    @Column(name = "blog_keywords", columnDefinition = "TEXT")
    var blogKeywords: String? = null

) : BaseEntity() {
    fun update(dto: UserFilterReqDto) {
        this.jobpostingKeywords = JobPostingKeyword(
            dto.jobNames,
            dto.locationNames,
            dto.jobTypeNames,
            dto.industryNames,
            dto.educationLevelNames
        )
        this.blogKeywords = dto.blogKeywords
    }
}
