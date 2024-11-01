package com.develetter.develetter.conference.converter

import com.develetter.develetter.conference.dto.ConferenceRegisterDto
import com.develetter.develetter.conference.dto.ConferenceResDto
import com.develetter.develetter.conference.entity.Conference

object Converter {

    // DTO -> Entity
    fun toEntity(dto: ConferenceRegisterDto): Conference {
        return Conference(
            name = dto.name,
            host = dto.host,
            applyStartDate = dto.applyStartDate,
            applyEndDate = dto.applyEndDate,
            startDate = dto.startDate,
            endDate = dto.endDate,
            url = dto.url
        )
    }

    // Entity -> DTO
    fun toDto(conference: Conference): ConferenceResDto {
        return ConferenceResDto(
            id = conference.id ?: 0L,
            name = conference.name,
            host = conference.host,
            applyStartDate = conference.applyStartDate,
            applyEndDate = conference.applyEndDate,
            startDate = conference.startDate,
            endDate = conference.endDate,
            url = conference.url
        )
    }
}
