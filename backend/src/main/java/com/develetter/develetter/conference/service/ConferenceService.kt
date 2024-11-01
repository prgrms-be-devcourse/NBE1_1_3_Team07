package com.develetter.develetter.conference.service

import com.develetter.develetter.conference.dto.ConferenceRegisterDto
import com.develetter.develetter.conference.dto.ConferenceResDto
import jakarta.validation.Valid
import java.time.LocalDate

interface ConferenceService {
    fun getAllConference(): List<ConferenceResDto>

    fun getAllConferenceWithDateRange(start: LocalDate, end: LocalDate): List<ConferenceResDto>

    fun createConference(@Valid conferenceRegisterDto: ConferenceRegisterDto)

    fun updateConference(id: Long, @Valid conferenceRegisterDto: ConferenceRegisterDto)

    fun deleteConference(id: Long)
}
