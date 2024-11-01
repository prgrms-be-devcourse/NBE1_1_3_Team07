package com.develetter.develetter.conference.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.URL
import java.time.LocalDate

data class ConferenceRegisterDto(
        @field:NotBlank(message = "컨퍼런스 이름은 필수입니다.")
        @field:Size(max = 255, message = "회의 이름은 255자 이내여야 합니다.")
        var name: String,

        @field:NotBlank(message = "주최는 필수입니다.")
        @field:Size(max = 255, message = "주최자는 255자 이내여야 합니다.")
        var host: String,

        @field:NotNull(message = "신청 시작 날짜는 필수입니다.")
        var applyStartDate: LocalDate,

        @field:NotNull(message = "신청 종료 날짜는 필수입니다.")
        @field:FutureOrPresent(message = "신청 종료 날짜는 현재 또는 미래여야 합니다.")
        var applyEndDate: LocalDate,

        @field:NotNull(message = "시작 날짜는 필수입니다.")
        @field:FutureOrPresent(message = "시작 날짜는 현재 또는 미래여야 합니다.")
        var startDate: LocalDate,

        @field:NotNull(message = "종료 날짜는 필수입니다.")
        @field:FutureOrPresent(message = "종료 날짜는 현재 또는 미래여야 합니다.")
        var endDate: LocalDate,

        @field:NotBlank(message = "URL은 필수입니다.")
        @field:URL(message = "유효한 URL이어야 합니다.")
        var url: String
)
