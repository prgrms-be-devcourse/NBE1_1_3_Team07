package com.develetter.develetter.blog.scheduler;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.repository.BlogRepository;
import com.develetter.develetter.blog.repository.FilteredBlogRepository;
import com.develetter.develetter.blog.service.InterestService;
import com.develetter.develetter.blog.service.SearchService;
import com.develetter.develetter.blog.Util.BlogUtil;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlogSchedulerImpl implements BlogScheduler {

    private final SearchService searchService;
    private final BlogRepository blogRepository;
    private final InterestService interestService;
    private final UserFilterRepository userFilterRepository;
    private final FilteredBlogRepository filteredBlogRepository;

    // 매주 월요일 자정에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    public void fetchAndStoreBlogData() {
        filteredBlogRepository.deleteOldRecords();
        // 블로그 테이블 초기화 (모든 데이터 삭제)
        blogRepository.deleteAll();
        blogRepository.resetAutoIncrement();

        // 관심사 목록을 가져와서 각각의 관심사에 대해 블로그 데이터를 저장
        List<String> interests = interestService.getInterests();
        interests.forEach(searchService::searchAndSaveBlogPosts);
        log.info("패치된 Blog 데이터가 저장되었습니다: {}", System.currentTimeMillis());

        // 모든 사용자 필터 조회
        List<UserFilter> userFilters = userFilterRepository.findAll();
        for (UserFilter userFilter : userFilters) {
            Long userId = userFilter.getUserId();
            List<String> interestsForUser = BlogUtil.getBlogKeywordsAsList(userFilter.getBlogKeywords());

            Blog blog = null;
            String randomInterest = null;

            // 관심사가 없는 경우 : AI 관련 블로그를 저장하도록 처리
            if (interestsForUser.isEmpty()) {
                blog = interestService.getRandomBlogBySearchQuery(userId, "AI");
            } else {
                // 관심사가 1개 이상 있는 경우 : 사용자 관심사 리스트 중 하나를 랜덤으로 선택
                randomInterest = interestsForUser.get(new Random().nextInt(interestsForUser.size()));

                // 랜덤으로 선택된 관심사에 맞는 중복되지 않은 블로그를 저장
                blog = interestService.getRandomBlogBySearchQuery(userId, randomInterest);
            }

            if (blog == null) {
                // 관심사에 맞는 블로그가 없을 경우 AI 관련 블로그로 대체
                blog = interestService.getRandomBlogBySearchQuery(userId, "AI");
                if (blog != null) {
                    log.info("사용자 {} 의 AI 관련 블로그 id {}을(를) 저장했습니다.", userId, blog.getId());
                } else {
                    log.warn("사용자 {} 의 관심사 및 AI 관련 블로그가 모두 없습니다.", userId); //주로 API 일일 할당량 관련 문제 발생할 시 이 오류가 발생
                }
            } else {
                log.info("사용자 {} 의 관심사 {} 관련 블로그 id {}을(를) 저장했습니다.", userId, randomInterest, blog.getId());
            }
        }
    }

}