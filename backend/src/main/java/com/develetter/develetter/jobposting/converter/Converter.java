package com.develetter.develetter.jobposting.converter;

import com.develetter.develetter.jobposting.dto.JobPostingEmailDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto.Job;
import com.develetter.develetter.jobposting.entity.JobPosting;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class Converter {

    // DTO -> Entity 변환 메서드
    public static JobPosting toEntity(Job job) {
        return JobPosting.builder()
                .url(job.url())
                .active(job.active() == 1)
                .companyName(getValue(job.company(), c -> c.detail().name()))
                .companyUrl(getValue(job.company(), c -> c.detail().href()))
                .title(getValue(job.position(), p -> p.title()))
                .industryCode(getValue(job.position(), p -> p.industry().code()))
                .industryName(getValue(job.position(), p -> p.industry().name()))
                .locationCode(getValue(job.position(), p -> p.location().code()))
                .locationName(getValue(job.position(), p -> p.location().name()))
                .jobTypeCode(getValue(job.position(), p -> p.jobType().code()))
                .jobTypeName(getValue(job.position(), p -> p.jobType().name()))
                .jobMidCode(getValue(job.position(), p -> Optional.ofNullable(p.jobMidCode()).map(j -> j.code()).orElse(null)))
                .jobMidName(getValue(job.position(), p -> Optional.ofNullable(p.jobMidCode()).map(j -> j.name()).orElse(null)))
                .jobCode(getValue(job.position(), p -> Optional.ofNullable(p.jobCode()).map(j -> j.code()).orElse(null)))
                .jobName(getValue(job.position(), p -> Optional.ofNullable(p.jobCode()).map(j -> j.name()).orElse(null)))
                .experienceCode(getValue(job.position(), p -> p.experienceLevel().code()))
                .experienceMin(getValue(job.position(), p -> p.experienceLevel().min()))
                .experienceMax(getValue(job.position(), p -> p.experienceLevel().max()))
                .experienceName(getValue(job.position(), p -> p.experienceLevel().name()))
                .educationLevelCode(getValue(job.position(), p -> p.requiredEducationLevel().code()))
                .educationLevelName(getValue(job.position(), p -> p.requiredEducationLevel().name()))
                .keyword(job.keyword())
                .salaryCode(getValue(job.salary(), s -> s.code()))
                .salaryName(getValue(job.salary(), s -> s.name()))
                .postingTimestamp(job.postingTimestamp())
                .postingDate(parseDate(job.postingDate()))
                .modificationTimestamp(job.modificationTimestamp())
                .openingTimestamp(job.openingTimestamp())
                .expirationTimestamp(job.expirationTimestamp())
                .expirationDate(parseDate(job.expirationDate()))
                .closeTypeCode(getValue(job.closeType(), c -> c.code()))
                .closeTypeName(getValue(job.closeType(), c -> c.name()))
                .readCount(Optional.ofNullable(job.readCnt()).orElse(0))
                .applyCount(Optional.ofNullable(job.applyCnt()).orElse(0))
                .build();
    }

    // 공통적인 Optional 처리 메서드
    private static <T, R> R getValue(T object, Function<T, R> mapper) {
        return Optional.ofNullable(object).map(mapper).orElse(null);
    }

    // 날짜 파싱 처리 메서드
    private static LocalDateTime parseDate(String date) {
        return date != null ? OffsetDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toLocalDateTime() : null;
    }

    public static JobPostingEmailDto toEmailDTO(JobPosting jobPosting) {
        return new JobPostingEmailDto(
                jobPosting.getUrl(),
                jobPosting.getCompanyName(),
                jobPosting.getTitle(),
                jobPosting.getIndustryName(),
                jobPosting.getLocationName(),
                jobPosting.getJobTypeName(),
                jobPosting.getExperienceName(),
                jobPosting.getPostingDate(),
                jobPosting.getExpirationDate()
        );
    }
}
