package com.develetter.develetter.conference.controller;

import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.conference.dto.ConferenceResDto;
import com.develetter.develetter.conference.service.ConferenceServiceImpl;
import com.develetter.develetter.global.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conference")
public class ConferenceController {

    private final ConferenceServiceImpl conferenceService;

    @GetMapping()
    public ApiResponseDto<List<ConferenceResDto>> getAllConferences() {
        return new ApiResponseDto<>(200, "컨퍼런스 조회 성공", conferenceService.getAllConference());
    }

    @PostMapping()
    public ApiResponseDto<Void> registerConference(@Valid @RequestBody ConferenceRegisterDto conferenceRegisterDto) {
        conferenceService.createConference(conferenceRegisterDto);
        return new ApiResponseDto<>(200, "컨퍼런스 생성 성공");
    }

    @PutMapping("/{id}")
    public ApiResponseDto<Void> updateConference(@PathVariable Long id, @Valid @RequestBody ConferenceRegisterDto conferenceRegisterDto) {
        conferenceService.updateConference(id, conferenceRegisterDto);
        return new ApiResponseDto<>(200, "컨퍼런스 수정 성공");
    }

    @DeleteMapping("/{id}")
    public ApiResponseDto<Void> deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
        return new ApiResponseDto<>(200, "컨퍼런스 삭제 성공");
    }

}
