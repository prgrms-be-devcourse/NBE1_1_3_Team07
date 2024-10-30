package com.develetter.develetter.blog.controller;


import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.service.SearchService;
import com.develetter.develetter.global.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping()
    public ApiResponseDto<Void> searchAndSave(@RequestParam String query) {
        searchService.searchAndSaveBlogPosts(query);
        return new ApiResponseDto<>(200, "검색 데이터가 성공적으로 저장되었습니다!");
    }

    @GetMapping("/blogs")
    public ApiResponseDto<List<Blog>> getAllBlogs() {
        return new ApiResponseDto<>(200, "블로그 조회 성공", searchService.getAllBlogs());
    }

}