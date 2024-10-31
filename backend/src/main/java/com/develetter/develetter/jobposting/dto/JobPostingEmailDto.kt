package com.develetter.develetter.jobposting.dto;

import java.time.LocalDateTime;

public record JobPostingEmailDto(
        String url,               // 채용공고 URL
        String companyName,       // 회사 이름
        String title,             // 채용공고 타이틀
        String industryName,      // 산업 이름
        String locationName,      // 위치 이름
        String jobTypeName,       // 직무 유형 (정규직, 프리랜서 등)
        String experienceName,    // 경력
        LocalDateTime postingDate,// 채용공고 시작일
        LocalDateTime expirationDate // 채용공고 마감일
) {}
