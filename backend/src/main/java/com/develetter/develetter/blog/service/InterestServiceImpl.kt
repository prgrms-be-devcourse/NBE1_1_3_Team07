package com.develetter.develetter.blog.service

import com.develetter.develetter.blog.dto.BlogDto
import com.develetter.develetter.blog.entity.Blog
import com.develetter.develetter.blog.entity.FilteredBlog
import com.develetter.develetter.blog.repository.BlogRepository
import com.develetter.develetter.blog.repository.FilteredBlogRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*

private val log = KotlinLogging.logger {}
@Service
class InterestServiceImpl(
    private val blogRepository: BlogRepository,
    private val filteredBlogRepository: FilteredBlogRepository
) : InterestService {

    override fun getInterests() = listOf(
        "Java", "JavaScript", "AI" /*,"CSS", "TypeScript", "Html","Javascript", "Next.js",
                "Swift", "Kotlin", "Java", "Spring", "Node.js", "Python"*/
    )

    // 사용자가 선택한 관심사 기반으로 받지 않은 랜덤 블로그 글을 반환
    override fun getRandomBlogBySearchQuery(userId: Long?, searchQuery: String?): Blog? {
        var blogs = blogRepository.findBlogsBySearchQuery(searchQuery)

        // 검색된 블로그가 없는 경우 AI 관련 블로그 검색
        if (blogs.isNullOrEmpty()) {
            blogs = blogRepository.findBlogsBySearchQuery("AI")
            if (blogs.isNullOrEmpty()) {
                log.info("AI 관련 블로그도 없습니다.") // 주로 API 일일 할당량 관련 문제 발생할 시 이 오류가 발생
                return null
            }
        }

        // 사용자가 받지 않은 블로그 필터링
        var unreceivedBlogs = blogs.filter { blog ->
            !filteredBlogRepository.existsByUserIdAndBlog(userId, blog?.id)
        }

        // 사용자가 받지 않은 블로그가 없는 경우, AI 관련 블로그 필터링
        if (unreceivedBlogs.isEmpty()) {
            unreceivedBlogs = blogRepository.findBlogsBySearchQuery("AI")?.filter { blog ->
                !filteredBlogRepository.existsByUserIdAndBlog(userId, blog?.id)
            } ?: emptyList()

            if (unreceivedBlogs.isEmpty()) {
                log.info("사용자가 받지 않은 AI 관련 블로그 글도 없습니다.") // 이런 경우는 웬만하면 없을 것
                return null
            }
        }

        // 여러 결과 중 하나를 랜덤으로 선택
        val randomBlog = unreceivedBlogs[Random().nextInt(unreceivedBlogs.size)]
        if (randomBlog != null) {
            saveFilteredBlog(userId, randomBlog.id ?: return null)
        }
        return randomBlog
    }

    private fun saveFilteredBlog(userId: Long?, blogId: Long) {
        val filteredBlog = FilteredBlog(
            userId = userId,
            blog = blogId
        )
        filteredBlogRepository.save(filteredBlog)
        log.info("FilteredBlog 저장 완료: user={}, blog={}", userId, blogId)
    }

    // userId로 BlogDto 반환하는 메서드
    override fun getBlogByUserId(userId: Long?): BlogDto? {
        // userId에 해당하는 가장 최근의 FilteredBlog를 조회
        val filteredBlog = filteredBlogRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
            ?.orElse(null)
        if (filteredBlog == null) {
            log.info("해당 사용자에 대한 필터된 블로그가 없습니다.")
            return null
        }

        // 해당 블로그 정보 조회
        val blog = filteredBlog.blog?.let { blogRepository.findById(it)
            .orElse(null)
        }
        if (blog == null) {
            log.info("해당 블로그가 없습니다.")
            return null
        }

        // BlogDto로 반환
        return BlogDto(blog.title ?: "", blog.link ?: "")
    }
}
