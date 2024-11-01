package com.develetter.develetter.conference.controller

import com.develetter.develetter.conference.dto.ConferenceRegisterDto
import com.develetter.develetter.conference.dto.ConferenceResDto
import com.develetter.develetter.conference.service.ConferenceServiceImpl
import com.develetter.develetter.global.dto.ApiResponseDto
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/conference")
class ConferenceController(
    private val conferenceService: ConferenceServiceImpl
) {

    @GetMapping
    fun getAllConferences(): ApiResponseDto<List<ConferenceResDto>> {
        return ApiResponseDto(200, "컨퍼런스 조회 성공", conferenceService.getAllConference())
    }

    @PostMapping
    fun registerConference(@Valid @RequestBody conferenceRegisterDto: ConferenceRegisterDto): ApiResponseDto<Void> {
        conferenceService.createConference(conferenceRegisterDto)
        return ApiResponseDto(200, "컨퍼런스 생성 성공")
    }

    @PutMapping("/{id}")
    fun updateConference(
        @PathVariable id: Long,
        @Valid @RequestBody conferenceRegisterDto: ConferenceRegisterDto
    ): ApiResponseDto<Void> {
        conferenceService.updateConference(id, conferenceRegisterDto)
        return ApiResponseDto(200, "컨퍼런스 수정 성공")
    }

    @DeleteMapping("/{id}")
    fun deleteConference(@PathVariable id: Long): ApiResponseDto<Void> {
        conferenceService.deleteConference(id)
        return ApiResponseDto(200, "컨퍼런스 삭제 성공")
    }
}
