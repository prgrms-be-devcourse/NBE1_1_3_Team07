package com.develetter.develetter.blog.service

import com.develetter.develetter.blog.entity.Blog

interface SearchService {
    // 특정 쿼리에 대해 Google API에서 검색하고, 결과를 블로그 포스트로 저장하는 메서드
    fun searchAndSaveBlogPosts(query: String?)
}
