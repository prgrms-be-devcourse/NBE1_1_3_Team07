package com.develetter.develetter.blog.repository

import com.develetter.develetter.blog.entity.Blog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface BlogRepository : JpaRepository<Blog?, Long?> {
    // 링크가 이미 존재하는지 확인
    fun existsByLink(link: String?): Boolean

    // 테이블의 AUTO_INCREMENT 값을 1로 리셋하는 쿼리
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE blog AUTO_INCREMENT = 1", nativeQuery = true)
    fun resetAutoIncrement()

    // 관심사에 맞는 블로그 글 중 랜덤으로 하나 선택
    @Query("SELECT b FROM Blog b WHERE b.title LIKE %:searchQuery% OR b.snippet LIKE %:searchQuery% ORDER BY RAND()")
    fun findBlogsBySearchQuery(@Param("searchQuery") searchQuery: String?): List<Blog?>?
    fun findById(id: Long): Optional<Blog?>
}
