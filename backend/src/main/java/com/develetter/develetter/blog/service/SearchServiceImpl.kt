package com.develetter.develetter.blog.service

import com.develetter.develetter.blog.Util.BlogUtil
import com.develetter.develetter.blog.entity.Blog
import com.develetter.develetter.blog.repository.BlogRepository
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

private val log = KotlinLogging.logger {}
@Service
@RequiredArgsConstructor
@Slf4j
class SearchServiceImpl (
    @Value("\${API_KEY}") private val apiKey: String,
    @Value("\${SEARCH_ENGINE_ID}") private val searchEngineId: String,
    private val webClient: WebClient,
    private val blogRepository: BlogRepository
) : SearchService {

    val GOOGLE_SEARCH_URL = "https://www.googleapis.com/customsearch/v1"

    override fun searchAndSaveBlogPosts(query: String?) {
        var startIndex = 1 // 검색 결과의 시작 인덱스 (페이징 처리 위해서 사용)
        var savedCount = 0 // 저장된 블로그 글 개수
        val requiredCount = 30 // 최소한 저장해야 할 세부 글 개수
        var hasMoreResults = true
        try {
            // 세부 글이 30개 저장될 때까지 반복
            while (hasMoreResults && savedCount < requiredCount) {
                val url = UriComponentsBuilder.fromHttpUrl(GOOGLE_SEARCH_URL)
                    .queryParam("key", apiKey)
                    .queryParam("cx", searchEngineId)
                    .queryParam("dateRestrict", "d700")
                    .queryParam("q", query)
                    .queryParam("num", 10)
                    .queryParam("start", startIndex) // 페이징을 위한 시작 인덱스
                    .toUriString()

                // WebClient로 API 호출
                val response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .block()

                // JSON 응답을 받아서 처리
                val jsonResponse = JSONObject(Objects.requireNonNull(response))
                // 검색 결과가 없으면 프로그램 종료
                if (!jsonResponse.has("items")) {
                    log.info("검색 결과가 없습니다.")
                    return
                }
                val items = jsonResponse.getJSONArray("items")
                if (items.isEmpty) {
                    hasMoreResults = false // 더 이상 결과가 없으면 중단
                }

                // 응답 데이터 반복 처리
                var i = 0
                while (i < items.length() && savedCount < requiredCount) {
                    val item = items.getJSONObject(i)

                    // 기본 데이터 추출
                    val title = item.getString("title")
                    val snippet = item.getString("snippet")
                    val link = item.getString("link")
                    if (!title.lowercase(Locale.getDefault()).contains(query!!.lowercase(Locale.getDefault())) &&
                        !snippet.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) &&
                        !link.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
                    ) {
                        i++
                        continue  // 검색어가 포함되지 않은 경우 해당 결과 건너뜀
                    }

                    // 세부 블로그 글인지 확인
                    if (BlogUtil.isBlogDetailPage(link)) {
                        // 중복된 링크인지 확인
                        if (!blogRepository.existsByLink(link)) {
                            // pagemap에서 추가 정보를 추출하고 데이터 저장
                            processPagemap(item, title, snippet, link)
                            savedCount++ // 저장된 블로그 글 수 증가
                        }
                    }
                    i++
                }

                // 다음 페이지로 이동
                startIndex += 10
            }
        } catch (e: Exception) {
            log.error("searchAndSaveBlogPosts에서 문제 발생", e)
        }
    }

    private fun processPagemap(item: JSONObject, title: String, snippet: String, link: String) {
        var title = title
        try {
            if (item.has("pagemap")) {
                val pagemap = item.getJSONObject("pagemap")

                // metatags로부터 Open Graph 데이터 추출
                if (pagemap.has("metatags")) {
                    val metatags = pagemap.getJSONArray("metatags").getJSONObject(0)

                    // Open Graph 제목이 있으면 덮어쓰기
                    if (metatags.has("og:title")) {
                        title = metatags.getString("og:title")
                        if (title.equals("Google for Developers Korea Blog", ignoreCase = true)) {
                            return
                        }
                    }
                }
            }

            // 3단계로 넘어가서 데이터를 저장
            saveBlogData(title, snippet, link)
        } catch (e: Exception) {
            log.error("processPagemap에서 문제 발생", e)
        }
    }

    private fun saveBlogData(title: String, snippet: String, link: String) {
        try {
            // Blog 객체 생성
            val blog = Blog()
            blog.title = title
            blog.snippet = snippet
            blog.link = link

            // 데이터베이스에 저장
            blogRepository.save(blog)

        } catch (e: Exception) {
            log.error("블로그 저장 문제 발생", e)
        }
    }
}
