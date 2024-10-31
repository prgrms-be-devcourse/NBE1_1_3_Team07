package com.develetter.develetter.jobposting.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class JobSearchResDto @JsonCreator constructor(
    @JsonProperty("jobs") val jobs: Jobs
) {
    data class Jobs @JsonCreator constructor(
        @JsonProperty("count") val count: Int,           // job 엘리먼트 개수
        @JsonProperty("start") val start: Int,           // 검색 결과의 페이지 번호
        @JsonProperty("total") val total: Int,           // 총 검색 결과 수
        @JsonProperty("job") val job: List<Job>?         // 채용공고 엘리먼트 목록
    )

    data class Job @JsonCreator constructor(
        @JsonProperty("url") val url: String?,                      // 채용공고 표준 URL
        @JsonProperty("active") val active: Int?,                   // 공고 진행 여부 (1: 진행중, 0: 마감)
        @JsonProperty("company") val company: Company?,             // 회사 정보
        @JsonProperty("position") val position: Position?,          // 직무 정보
        @JsonProperty("keyword") val keyword: String?,              // 키워드
        @JsonProperty("salary") val salary: Salary?,                // 연봉 정보
        @JsonProperty("id") val id: Long?,                          // 공고 번호
        @JsonProperty("posting-timestamp") val postingTimestamp: Long?,   // 게시일 Unix timestamp
        @JsonProperty("posting-date") val postingDate: String?,      // 게시일 (선택: 날짜/시간 형식)
        @JsonProperty("modification-timestamp") val modificationTimestamp: Long?,  // 수정일 Unix timestamp
        @JsonProperty("opening-timestamp") val openingTimestamp: Long?,  // 접수 시작일 Unix timestamp
        @JsonProperty("expiration-timestamp") val expirationTimestamp: Long?,  // 마감일 Unix timestamp
        @JsonProperty("expiration-date") val expirationDate: String?,    // 마감일 (선택: 날짜/시간 형식)
        @JsonProperty("close-type") val closeType: CloseType?,           // 마감일 형식
        @JsonProperty("read-cnt") val readCnt: Int?,                     // 조회수 (선택)
        @JsonProperty("apply-cnt") val applyCnt: Int?                    // 지원자수 (선택)
    )

    data class Company @JsonCreator constructor(
        @JsonProperty("detail") val detail: Detail?                    // 기업 세부 정보
    )

    data class Detail @JsonCreator constructor(
        @JsonProperty("href") val href: String?,                     // 기업 정보 페이지
        @JsonProperty("name") val name: String?                      // 기업명
    )

    data class Position @JsonCreator constructor(
        @JsonProperty("title") val title: String?,                   // 공고 제목
        @JsonProperty("industry") val industry: Industry?,           // 업종 정보
        @JsonProperty("location") val location: Location?,           // 지역 정보
        @JsonProperty("job-type") val jobType: JobType?,             // job-type 필드
        @JsonProperty("job-mid-code") val jobMidCode: JobMidCode?,   // job-mid-code 필드
        @JsonProperty("job-code") val jobCode: JobCode?,             // job-code 필드
        @JsonProperty("experience-level") val experienceLevel: ExperienceLevel?, // experience-level 필드
        @JsonProperty("required-education-level") val requiredEducationLevel: RequiredEducationLevel? // required-education-level 필드
    )

    data class Industry @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 업종 코드
        @JsonProperty("name") val name: String?                      // 업종명
    )

    data class Location @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 지역 코드
        @JsonProperty("name") val name: String?                      // 지역명
    )

    data class JobType @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 근무형태 코드
        @JsonProperty("name") val name: String?                      // 근무형태 값
    )

    data class JobMidCode @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 상위 직무 코드
        @JsonProperty("name") val name: String?                      // 상위 직무명
    )

    data class JobCode @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 직무 코드
        @JsonProperty("name") val name: String?                      // 직무명
    )

    data class ExperienceLevel @JsonCreator constructor(
        @JsonProperty("code") val code: Int?,                        // 경력 코드 (1: 신입, 2: 경력, 3: 신입/경력, 0: 경력무관)
        @JsonProperty("min") val min: Int?,                          // 경력 최소 값
        @JsonProperty("max") val max: Int?,                          // 경력 최대 값
        @JsonProperty("name") val name: String?                      // 경력 값
    )

    data class RequiredEducationLevel @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 학력 코드
        @JsonProperty("name") val name: String?                      // 학력 값
    )

    data class CloseType @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 마감일 코드 (1: 접수 마감일, 2: 채용시, 3: 상시, 4: 수시)
        @JsonProperty("name") val name: String?                      // 마감일 값
    )

    data class Salary @JsonCreator constructor(
        @JsonProperty("code") val code: String?,                     // 연봉 코드
        @JsonProperty("name") val name: String?                      // 연봉 값
    )
}