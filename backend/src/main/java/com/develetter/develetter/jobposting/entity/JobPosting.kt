package com.develetter.develetter.jobposting.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "job_posting")
class JobPosting(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "url", nullable = false, length = 255)
    val url: String? = null,

    @Column(name = "active", nullable = false)
    val active: Boolean = true,

    @Column(name = "company_name", nullable = false, length = 255)
    val companyName: String? = null,

    @Column(name = "company_url", length = 255)
    val companyUrl: String? = null,

    @Column(name = "title", nullable = false, length = 255)
    val title: String? = null,

    @Column(name = "industry_code", length = 50)
    val industryCode: String? = null,

    @Column(name = "industry_name", length = 255)
    val industryName: String? = null,

    @Column(name = "location_code", length = 255)
    val locationCode: String? = null,

    @Column(name = "location_name", length = 255)
    val locationName: String? = null,

    @Column(name = "job_type_code", length = 255)
    val jobTypeCode: String? = null,

    @Column(name = "job_type_name", length = 255)
    val jobTypeName: String? = null,

    @Column(name = "job_mid_code", length = 255)
    val jobMidCode: String? = null,

    @Column(name = "job_mid_name", length = 255)
    val jobMidName: String? = null,

    @Column(name = "job_code", columnDefinition = "TEXT")
    val jobCode: String? = null,

    @Column(name = "job_name", columnDefinition = "TEXT")
    val jobName: String? = null,

    @Column(name = "experience_code")
    val experienceCode: Int? = null,

    @Column(name = "experience_min")
    val experienceMin: Int? = null,

    @Column(name = "experience_max")
    val experienceMax: Int? = null,

    @Column(name = "experience_name", length = 255)
    val experienceName: String? = null,

    @Column(name = "education_level_code", length = 50)
    val educationLevelCode: String? = null,

    @Column(name = "education_level_name", length = 255)
    val educationLevelName: String? = null,

    @Column(name = "keyword", columnDefinition = "TEXT")
    val keyword: String? = null,

    @Column(name = "salary_code", length = 50)
    val salaryCode: String? = null,

    @Column(name = "salary_name", length = 255)
    val salaryName: String? = null,

    @Column(name = "posting_timestamp")
    val postingTimestamp: Long? = null,

    @Column(name = "posting_date")
    val postingDate: LocalDateTime? = null,

    @Column(name = "modification_timestamp")
    val modificationTimestamp: Long? = null,

    @Column(name = "opening_timestamp")
    val openingTimestamp: Long? = null,

    @Column(name = "expiration_timestamp")
    val expirationTimestamp: Long? = null,

    @Column(name = "expiration_date")
    val expirationDate: LocalDateTime? = null,

    @Column(name = "close_type_code", length = 50)
    val closeTypeCode: String? = null,

    @Column(name = "close_type_name", length = 255)
    val closeTypeName: String? = null,

    @Column(name = "read_count", nullable = false)
    val readCount: Int = 0,

    @Column(name = "apply_count", nullable = false)
    val applyCount: Int = 0

) : BaseEntity()
