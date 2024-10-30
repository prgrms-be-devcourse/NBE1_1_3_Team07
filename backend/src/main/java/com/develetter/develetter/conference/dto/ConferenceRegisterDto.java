package com.develetter.develetter.conference.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public record ConferenceRegisterDto(
        @NotBlank(message = "컨퍼런스 이름은 필수입니다.")
        @Size(max = 255, message = "회의 이름은 255자 이내여야 합니다.")
        String name,

        @NotBlank(message = "주최는 필수입니다.")
        @Size(max = 255, message = "주최자는 255자 이내여야 합니다.")
        String host,

        @NotNull(message = "신청 시작 날짜는 필수입니다.")
        LocalDate applyStartDate,

        @NotNull(message = "신청 종료 날짜는 필수입니다.")
        @FutureOrPresent(message = "신청 종료 날짜는 현재 또는 미래여야 합니다.")
        LocalDate applyEndDate,

        @NotNull(message = "시작 날짜는 필수입니다.")
        @FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
        LocalDate startDate,

        @NotNull(message = "종료 날짜는 필수입니다.")
        @FutureOrPresent(message = "종료 날짜는 현재 또는 미래여야 합니다.")
        LocalDate endDate,

        @NotBlank(message = "URL은 필수입니다.")
        @URL(message = "유효한 URL이어야 합니다.")
        String url
) {}

