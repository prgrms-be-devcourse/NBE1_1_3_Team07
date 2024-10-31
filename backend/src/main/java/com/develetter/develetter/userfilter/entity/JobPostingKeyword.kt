package com.develetter.develetter.userfilter.entity

import jakarta.persistence.Embeddable

@Embeddable
data class JobPostingKeyword(
    var jobNames: String? = null,           // 직무명
    var locationNames: String? = null,      // 위치
    var jobTypeNames: String? = null,       // 직무 유형
    var industryNames: String? = null,      // 산업
    var educationLevelNames: String? = null // 학력 요건
)