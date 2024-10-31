package com.develetter.develetter.userfilter.dto;

import com.develetter.develetter.userfilter.entity.JobPostingKeyword;
import com.develetter.develetter.userfilter.entity.UserFilter;

public record UserFilterReqDto(
        String jobNames,            // 직무명 (예: "자바, 스프링, 파이썬")
        String locationNames,       // 위치 (예: "대구, 서울")
        String jobTypeNames,        // 직무 유형 (예: "정규직, 계약직")
        String industryNames,       // 산업 (예: "IT, 금융")
        String educationLevelNames, // 학력 요건 (예: "학사, 석사")
        String blogKeywords         // 블로그
) {

    // UserFilter 엔티티로 변환하는 정적 메서드
    public static UserFilter toEntity(Long userId, UserFilterReqDto dto) {
        // JobPostingKeyword 객체를 생성하여 임베디드 필드에 할당
        JobPostingKeyword jobPostingKeyword = new JobPostingKeyword(
                dto.jobNames(),
                dto.locationNames(),
                dto.jobTypeNames(),
                dto.industryNames(),
                dto.educationLevelNames()
        );

        // UserFilter 엔티티를 생성하여 반환
        return UserFilter.builder()
                .userId(userId)
                .jobpostingKeywords(jobPostingKeyword)
                .blogKeywords(dto.blogKeywords())
                .build();
    }
}