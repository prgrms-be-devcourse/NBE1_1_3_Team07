package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.dto.BlogDto;
import com.develetter.develetter.blog.entity.Blog;

import java.util.List;

public interface InterestService {
    //관심사 목록을 반환하는 메서드
    List<String> getInterests();

    // 사용자가 선택한 관심사 기반으로 랜덤 블로그 글을 반환하는 메서드
    Blog getRandomBlogBySearchQuery(Long userId, String searchQuery);

    BlogDto getBlogByUserId(Long userId);

}
