package com.develetter.develetter.blog.service;

import com.develetter.develetter.blog.Util.BlogUtil;
import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements SearchService{
    private static final String DEFAULT_IMAGE_URL = "https://img.freepik.com/premium-vector/crescent-moon-shining-starry-night-sky_1334819-5377.jpg";

    @Value("${API_KEY}")
    private String API_KEY;

    @Value("${SEARCH_ENGINE_ID}")
    private String SEARCH_ENGINE_ID;
    //최근 두달이내 작성한 글만 불러오게 설정
    private final String GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1";

    private final WebClient webClient;
    private final BlogRepository blogRepository;

    public void searchAndSaveBlogPosts(String query) {
        int startIndex = 1;  // 검색 결과의 시작 인덱스 (페이징 처리 위해서 사용)
        int savedCount = 0;  // 저장된 블로그 글 개수
        int requiredCount = 30;  // 최소한 저장해야 할 세부 글 개수
        boolean hasMoreResults = true;

        try {
            // 세부 글이 10개 저장될 때까지 반복
            while (hasMoreResults && savedCount < requiredCount) {

                String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_SEARCH_URL)
                        .queryParam("key", API_KEY)
                        .queryParam("cx", SEARCH_ENGINE_ID)
                        .queryParam("dateRestrict", "d700")
                        .queryParam("q", query)
                        .queryParam("num", 10)
                        .queryParam("start", startIndex)  // 페이징을 위한 시작 인덱스
                        .toUriString();

                // WebClient로 API 호출
                String response = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

                // JSON 응답을 받아서 처리
                JSONObject jsonResponse = new JSONObject(Objects.requireNonNull(response));
                // 검색 결과가 없으면 프로그램 종료
                if (!jsonResponse.has("items")) {
                    log.info("검색 결과가 없습니다.");
                    return;
                }
                JSONArray items = jsonResponse.getJSONArray("items");

                if (items.isEmpty()) {
                    hasMoreResults = false;  // 더 이상 결과가 없으면 중단
                }

                // 응답 데이터 반복 처리
                for (int i = 0; i < items.length() && savedCount < requiredCount; i++) {
                    JSONObject item = items.getJSONObject(i);

                    // 기본 데이터 추출
                    String title = item.getString("title");
                    String snippet = item.getString("snippet");
                    String link = item.getString("link");

                    if (!title.toLowerCase().contains(query.toLowerCase()) &&
                            !snippet.toLowerCase().contains(query.toLowerCase()) &&
                            !link.toLowerCase().contains(query.toLowerCase())) {
                        continue;  // 검색어가 포함되지 않은 경우 해당 결과 건너뜀
                    }

                    // 세부 블로그 글인지 확인
                    if (BlogUtil.isBlogDetailPage(link)) {
                        // 중복된 링크인지 확인
                        if (!blogRepository.existsByLink(link)) {
                            // pagemap에서 추가 정보를 추출하고 데이터 저장
                            processPagemap(item, title, snippet, link);
                            savedCount++;  // 저장된 블로그 글 수 증가
                        }
                    }
                }

                // 다음 페이지로 이동
                startIndex += 10;
            }

        } catch (Exception e) {
            log.error("searchAndSaveBlogPosts에서 문제 발생", e);
        }
    }

    private void processPagemap(JSONObject item, String title, String snippet, String link) {
        String imageUrl = null;

        try {
            if (item.has("pagemap")) {
                JSONObject pagemap = item.getJSONObject("pagemap");

                // 1. cse_image로부터 이미지 URL 추출
                if (pagemap.has("cse_image")) {
                    JSONArray cseImageArray = pagemap.getJSONArray("cse_image");
                    imageUrl = cseImageArray.getJSONObject(0).getString("src");
                }

                // 2. metatags로부터 Open Graph 데이터 추출 (og:title, og:image 등)
                if (pagemap.has("metatags")) {
                    JSONObject metatags = pagemap.getJSONArray("metatags").getJSONObject(0);

                    // Open Graph 제목이 있으면 덮어쓰기
                    if (metatags.has("og:title")) {
                        title = metatags.getString("og:title");

                        if (title.equalsIgnoreCase("Google for Developers Korea Blog")) {
                            return;
                        }
                    }

                    // Open Graph 이미지 URL이 있으면 덮어쓰기
                    if (metatags.has("og:image")) {
                        imageUrl = metatags.getString("og:image");
                    }
                }
            }

            // 3단계로 넘어가서 데이터를 저장
            saveBlogData(title, snippet, link, imageUrl);

        } catch (Exception e) {
            log.error("processPagemap에서 문제 발생", e);
        }
    }

    private void saveBlogData(String title, String snippet, String link, String imageUrl) {
        try {
            // 이미지 URL이 없으면 기본 이미지 사용
            if (imageUrl == null || imageUrl.isEmpty()) {
                imageUrl = DEFAULT_IMAGE_URL;
            }

            // Blog 객체 생성
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setSnippet(snippet);
            blog.setLink(link);
            blog.setImageUrl(imageUrl);

            // 데이터베이스에 저장
            blogRepository.save(blog);

            //log.info("블로그 저장 완료: title={}, snippet={}, link={}, imageUrl={}", title, snippet, link, imageUrl);

        } catch (Exception e) {
            log.error("블로그 저장 문제 발생", e);
        }
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
}



