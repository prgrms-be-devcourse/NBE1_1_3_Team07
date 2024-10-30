package com.develetter.develetter.conference.service;

import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.conference.dto.ConferenceResDto;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface ConferenceService {
    List<ConferenceResDto> getAllConference();

    List<ConferenceResDto> getAllConferenceWithDateRange(LocalDate start, LocalDate end);

    void createConference(ConferenceRegisterDto conferenceRegisterDto);

    void updateConference(Long id, ConferenceRegisterDto conferenceRegisterDto);

    void deleteConference(Long id);
}
