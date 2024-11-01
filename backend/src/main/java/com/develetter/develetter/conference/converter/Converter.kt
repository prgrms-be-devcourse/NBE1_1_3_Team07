package com.develetter.develetter.conference.converter;

import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.conference.dto.ConferenceResDto;
import com.develetter.develetter.conference.entity.Conference;

public class Converter {

    // DTO -> Entity
    public static Conference toEntity(ConferenceRegisterDto dto) {
        return Conference.builder()
                .name(dto.name())
                .host(dto.host())
                .applyStartDate(dto.applyStartDate())
                .applyEndDate(dto.applyEndDate())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .url(dto.url())
                .build();
    }

    // Entity -> DTO
    public static ConferenceResDto toDto(Conference conference) {
        return new ConferenceResDto(
                conference.getId(),
                conference.getName(),
                conference.getHost(),
                conference.getApplyStartDate(),
                conference.getApplyEndDate(),
                conference.getStartDate(),
                conference.getEndDate(),
                conference.getUrl()
        );
    }
}
