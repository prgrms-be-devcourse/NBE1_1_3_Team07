package com.develetter.develetter.blog.scheduler

import com.develetter.develetter.blog.Util.BlogUtil
import com.develetter.develetter.blog.entity.Blog
import com.develetter.develetter.blog.repository.BlogRepository
import com.develetter.develetter.blog.repository.FilteredBlogRepository
import com.develetter.develetter.blog.service.InterestService
import com.develetter.develetter.blog.service.SearchService
import com.develetter.develetter.userfilter.repository.UserFilterRepository

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*
import mu.KotlinLogging

private val log = KotlinLogging.logger {}
@Component
class BlogSchedulerImpl(
    private val searchService: SearchService,
    private val blogRepository: BlogRepository,
    private val interestService: InterestService,
    private val userFilterRepository: UserFilterRepository,
    private val filteredBlogRepository: FilteredBlogRepository
) : BlogScheduler {

    // 매주 월요일 자정에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    //@Scheduled(fixedRate = 20000)
    @Transactional
    override fun fetchAndStoreBlogData() {
        filteredBlogRepository.deleteOldRecords()
        // 블로그 테이블 초기화 (모든 데이터 삭제)
        blogRepository.deleteAll()
        blogRepository.resetAutoIncrement()

        // 관심사 목록을 가져와서 각각의 관심사에 대해 블로그 데이터를 저장
        val interests: List<String> = interestService.getInterests()
        interests.forEach { query -> searchService.searchAndSaveBlogPosts(query) }
        log.info { "패치된 Blog 데이터가 저장되었습니다: ${System.currentTimeMillis()}" }

        // 모든 사용자 필터 조회
        val userFilters = userFilterRepository.findAll()
        for (userFilter in userFilters) {
            val userId = userFilter.userId
            val interestsForUser = BlogUtil.getBlogKeywordsAsList(userFilter.blogKeywords ?: "")
            var blog: Blog? = null
            var randomInterest: String? = null

            // 관심사가 없는 경우 : AI 관련 블로그를 저장하도록 처리
            if (interestsForUser.isEmpty()) {
                randomInterest = "AI"
                blog = interestService.getRandomBlogBySearchQuery(userId, randomInterest)
            } else {
                // 관심사가 1개 이상 있는 경우 : 사용자 관심사 리스트 중 하나를 랜덤으로 선택
                randomInterest = interestsForUser[Random().nextInt(interestsForUser.size)]
                blog = interestService.getRandomBlogBySearchQuery(userId, randomInterest)
            }

            if (blog == null) {
                randomInterest = "AI"
                blog = interestService.getRandomBlogBySearchQuery(userId, randomInterest)
                if (blog != null) {
                    log.info { "사용자 $userId 의 $randomInterest 관련 블로그 id ${blog.id}을(를) 저장했습니다." }
                } else {
                    log.warn { "사용자 $userId 의 관심사 및 AI 관련 블로그가 모두 없습니다." }
                }
            } else {
                log.info { "사용자 $userId 의 관심사 $randomInterest 관련 블로그 id ${blog.id}을(를) 저장했습니다." }
            }
        }
    }
}
