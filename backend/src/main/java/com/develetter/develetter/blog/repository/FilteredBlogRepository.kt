package com.develetter.develetter.blog.repository

import com.develetter.develetter.blog.entity.FilteredBlog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FilteredBlogRepository : JpaRepository<FilteredBlog?, Long?> {
    fun existsByUserIdAndBlog(userId: Long?, blog: Long?): Boolean // UserFilter 기반 블로그 중복 확인

    // 특정 userId에 해당하는 가장 최근의 FilteredBlog를 반환
    fun findTopByUserIdOrderByCreatedAtDesc(userId: Long?): Optional<FilteredBlog?>?

    // 3개월 이상된 레코드를 삭제하는 쿼리 메서드
    @Modifying
    @Query(value = "DELETE FROM filtered_blog WHERE created_at < NOW() - INTERVAL 3 MONTH", nativeQuery = true)
    fun deleteOldRecords()
}
