package com.develetter.develetter.conference.dto;

import java.time.LocalDate;

public record ConferenceResDto(
        Long id,
        String name,
        String host,
        LocalDate applyStartDate,
        LocalDate applyEndDate,
        LocalDate startDate,
        LocalDate endDate,
        String url
) {
}
