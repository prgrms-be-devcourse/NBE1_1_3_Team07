package com.develetter.develetter.conference.service

import com.develetter.develetter.conference.converter.Converter
import com.develetter.develetter.conference.dto.ConferenceRegisterDto
import com.develetter.develetter.conference.dto.ConferenceResDto
import com.develetter.develetter.conference.entity.Conference
import com.develetter.develetter.conference.repository.ConferenceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class ConferenceServiceImpl(
    private val conferenceRepository: ConferenceRepository
) : ConferenceService {

    override fun getAllConference(): List<ConferenceResDto> {
        return conferenceRepository.findAll().map { Converter.toDto(it) }
    }

    override fun getAllConferenceWithDateRange(start: LocalDate, end: LocalDate): List<ConferenceResDto> {
        val conferences = conferenceRepository.findByDateRange(start, end)
        return conferences.map { Converter.toDto(it) }
    }

    @Transactional
    override fun createConference(conferenceRegisterDto: ConferenceRegisterDto) {
        val conference = Converter.toEntity(conferenceRegisterDto)
        conferenceRepository.save(conference)
    }

    @Transactional
    override fun updateConference(id: Long, conferenceRegisterDto: ConferenceRegisterDto) {
        val findConference = conferenceRepository.findById(id)
            .orElseThrow { IllegalArgumentException("해당 ID의 컨퍼런스를 찾을 수 없습니다. $id") }

        findConference.updateConference(conferenceRegisterDto)
    }

    @Transactional
    override fun deleteConference(id: Long) {
        if (!conferenceRepository.existsById(id)) {
            throw IllegalArgumentException("해당 ID의 컨퍼런스를 찾을 수 없습니다. $id")
        }

        conferenceRepository.deleteById(id)
    }
}
