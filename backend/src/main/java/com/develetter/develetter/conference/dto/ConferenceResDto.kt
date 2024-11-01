package com.develetter.develetter.conference.dto

import java.time.LocalDate

data class ConferenceResDto(
    val id: Long,
    var name: String,
    var host: String,
    var applyStartDate: LocalDate,
    var applyEndDate: LocalDate,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var url: String
)
