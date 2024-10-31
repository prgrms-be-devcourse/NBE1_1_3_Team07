package com.develetter.develetter.jobposting.dto

import java.time.LocalDateTime

data class JobPostingEmailDto(
    val url: String,               // 채용공고 URL
    val companyName: String,       // 회사 이름
    val title: String,             // 채용공고 타이틀
    val industryName: String,      // 산업 이름
    val locationName: String,      // 위치 이름
    val jobTypeName: String,       // 직무 유형 (정규직, 프리랜서 등)
    val experienceName: String,    // 경력
    val postingDate: LocalDateTime, // 채용공고 시작일
    val expirationDate: LocalDateTime // 채용공고 마감일
)
