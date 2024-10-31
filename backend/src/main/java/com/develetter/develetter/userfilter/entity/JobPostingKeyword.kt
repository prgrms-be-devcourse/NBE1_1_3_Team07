package com.develetter.develetter.userfilter.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingKeyword {

    // TODO: ENUM

    private String jobNames;            // 직무명
    private String locationNames;       // 위치
    private String jobTypeNames;        // 직무 유형
    private String industryNames;       // 산업
    private String educationLevelNames; // 학력 요건
}