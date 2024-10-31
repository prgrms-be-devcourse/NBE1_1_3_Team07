package com.develetter.develetter.userfilter.dto

import com.develetter.develetter.userfilter.entity.JobPostingKeyword
import com.develetter.develetter.userfilter.entity.UserFilter

data class UserFilterReqDto(
    val jobNames: String?,            // 직무명 (예: "자바, 스프링, 파이썬")
    val locationNames: String?,       // 위치 (예: "대구, 서울")
    val jobTypeNames: String?,        // 직무 유형 (예: "정규직, 계약직")
    val industryNames: String?,       // 산업 (예: "IT, 금융")
    val educationLevelNames: String?, // 학력 요건 (예: "학사, 석사")
    val blogKeywords: String?         // 블로그
) {
    companion object {
        // UserFilter 엔티티로 변환하는 메서드
        fun toEntity(userId: Long, dto: UserFilterReqDto): UserFilter {
            val jobPostingKeyword = JobPostingKeyword(
                dto.jobNames,
                dto.locationNames,
                dto.jobTypeNames,
                dto.industryNames,
                dto.educationLevelNames
            )

            return UserFilter(
                userId = userId,
                jobpostingKeywords = jobPostingKeyword,
                blogKeywords = dto.blogKeywords
            )
        }
    }
}