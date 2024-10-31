package com.develetter.develetter.jobposting.entity;

import com.develetter.develetter.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "job_posting")
public class JobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", nullable = false, length = 255)
    private String url;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "company_url", length = 255)
    private String companyUrl;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "industry_code", length = 50)
    private String industryCode;

    @Column(name = "industry_name", length = 255)
    private String industryName;

    @Column(name = "location_code", length = 255)
    private String locationCode;

    @Column(name = "location_name", length = 255)
    private String locationName;

    @Column(name = "job_type_code", length = 255)
    private String jobTypeCode;

    @Column(name = "job_type_name", length = 255)
    private String jobTypeName;

    @Column(name = "job_mid_code", length = 255)
    private String jobMidCode;

    @Column(name = "job_mid_name", length = 255)
    private String jobMidName;

    @Column(name = "job_code", columnDefinition = "TEXT")
    private String jobCode;

    @Column(name = "job_name", columnDefinition = "TEXT")
    private String jobName;

    @Column(name = "experience_code")
    private Integer experienceCode;

    @Column(name = "experience_min")
    private Integer experienceMin;

    @Column(name = "experience_max")
    private Integer experienceMax;

    @Column(name = "experience_name", length = 255)
    private String experienceName;

    @Column(name = "education_level_code", length = 50)
    private String educationLevelCode;

    @Column(name = "education_level_name", length = 255)
    private String educationLevelName;

    @Column(name = "keyword", columnDefinition = "TEXT")
    private String keyword;

    @Column(name = "salary_code", length = 50)
    private String salaryCode;

    @Column(name = "salary_name", length = 255)
    private String salaryName;

    @Column(name = "posting_timestamp")
    private Long postingTimestamp;

    @Column(name = "posting_date")
    private LocalDateTime postingDate;

    @Column(name = "modification_timestamp")
    private Long modificationTimestamp;

    @Column(name = "opening_timestamp")
    private Long openingTimestamp;

    @Column(name = "expiration_timestamp")
    private Long expirationTimestamp;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "close_type_code", length = 50)
    private String closeTypeCode;

    @Column(name = "close_type_name", length = 255)
    private String closeTypeName;

    @Column(name = "read_count", nullable = false)
    private Integer readCount = 0;

    @Column(name = "apply_count", nullable = false)
    private Integer applyCount = 0;

}
