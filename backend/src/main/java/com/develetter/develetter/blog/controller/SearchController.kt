package com.develetter.develetter.blog.controller

import com.develetter.develetter.blog.entity.Blog
import com.develetter.develetter.blog.service.SearchService
import com.develetter.develetter.global.dto.ApiResponseDto
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
class SearchController {
    private val searchService: SearchService? = null
    @GetMapping
    fun searchAndSave(@RequestParam query: String?): ApiResponseDto<Void> {
        searchService!!.searchAndSaveBlogPosts(query)
        return ApiResponseDto(200, "검색 데이터가 성공적으로 저장되었습니다!")
    }
}