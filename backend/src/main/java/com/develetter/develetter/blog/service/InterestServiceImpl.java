package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.dto.BlogDto;
import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.entity.FilteredBlog;
import com.develetter.develetter.blog.repository.BlogRepository;
import com.develetter.develetter.blog.repository.FilteredBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterestServiceImpl implements InterestService{

    private final BlogRepository blogRepository;
    private final FilteredBlogRepository filteredBlogRepository;

    // 관심사 목록을 반환하는 메서드, 이곳에서 관심사 키워드를 관리함
    public List<String> getInterests() {
        return List.of("Java", "JavaScript", "AI"/*,"CSS", "TypeScript", "Html","Javascript", "Next.js",
                "Swift", "Kotlin", "Java", "Spring", "Node.js", "Python"*/);
    }

    // 사용자가 선택한 관심사 기반으로 받지 않은 랜덤 블로그 글을 반환
    public Blog getRandomBlogBySearchQuery(Long userId, String searchQuery) {
        List<Blog> blogs = blogRepository.findBlogsBySearchQuery(searchQuery);

        // 검색된 블로그가 없는 경우 AI 관련 블로그 검색
        if (blogs.isEmpty()) {
            blogs = blogRepository.findBlogsBySearchQuery("AI");
            if (blogs.isEmpty()) {
                log.info("AI 관련 블로그도 없습니다."); //주로 API 일일 할당량 관련 문제 발생할 시 이 오류가 발생
                return null;
            }
        }

        // 사용자가 받지 않은 블로그 필터링
        List<Blog> unreceivedBlogs = blogs.stream()
                .filter(blog -> !filteredBlogRepository.existsByUserIdAndBlog(userId, blog.getId()))
                .toList();

        // 사용자가 받지 않은 블로그가 없는 경우, AI 관련 블로그 필터링
        if (unreceivedBlogs.isEmpty()) {
            unreceivedBlogs = blogRepository.findBlogsBySearchQuery("AI").stream()
                    .filter(blog -> !filteredBlogRepository.existsByUserIdAndBlog(userId, blog.getId()))
                    .toList();

            if (unreceivedBlogs.isEmpty()) {
                log.info("사용자가 받지 않은 AI 관련 블로그 글도 없습니다."); //이런 경우는 웬만하면 없을 것
                return null;
            }
        }

        // 여러 결과 중 하나를 랜덤으로 선택
        Blog randomBlog = unreceivedBlogs.get(new Random().nextInt(unreceivedBlogs.size()));

        saveFilteredBlog(userId, randomBlog.getId());

        return randomBlog;
    }

    private void saveFilteredBlog(Long userId, Long blogId) {
        FilteredBlog filteredBlog = FilteredBlog.builder()
                .userId(userId)
                .blog(blogId)
                .build();
        filteredBlogRepository.save(filteredBlog);
        log.info("FilteredBlog 저장 완료: user={}, blog={}", userId, blogId);
    }

    // 필터된 블로그 id로 blogDto 반환하는 메서드
    public BlogDto getBlogByUserId(Long userId) {
        // userId에 해당하는 가장 최근의 FilteredBlog를 조회
        FilteredBlog filteredBlog = filteredBlogRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElse(null);

        if (filteredBlog == null) {
            log.info("해당 사용자에 대한 필터된 블로그가 없습니다.");
            return null;
        }

        // 해당 블로그 정보 조회
        Blog blog = blogRepository.findById(filteredBlog.getBlog())
                .orElse(null);

        if (blog == null) {
            log.info("해당 블로그가 없습니다.");
            return null;
        }

        // BlogDto로 반환
        return new BlogDto(blog.getTitle(), blog.getLink());
    }
}

