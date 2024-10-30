package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.entity.Blog;
import java.util.List;

public interface SearchService {

    // 특정 쿼리에 대해 Google API에서 검색하고, 결과를 블로그 포스트로 저장하는 메서드
    void searchAndSaveBlogPosts(String query);

    // 저장된 모든 블로그 포스트를 반환하는 메서드
    List<Blog> getAllBlogs();
}
