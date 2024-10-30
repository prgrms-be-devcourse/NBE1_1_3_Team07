package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.global.util.DtoUtil;
import com.develetter.develetter.jobposting.converter.Converter;
import com.develetter.develetter.jobposting.dto.JobPostingEmailDto;
import com.develetter.develetter.jobposting.dto.JobSearchParams;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.exception.JobSearchException;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    @Value("${api.saramin.accesskey}")
    private String accessKey;

    @Value("${api.saramin.baseurl}")
    private String baseUrl;

    private final WebClient webClient;
    private final JobPostingRepository jobPostingRepository;
    private final FilteredJobPostingRepository filteredJobPostingRepository;

    @Override
    public JobSearchResDto searchJobs(int startPage, String sevenDaysAgo) {
        try {
            Map<String, Object> params = DtoUtil.toMap(JobSearchParams.defaultParams(startPage, sevenDaysAgo));

            JobSearchResDto jobSearchResDto = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder
                                .scheme("https")  // 스키마 설정
                                .host(baseUrl)    // 호스트 설정
                                .path("/job-search")  // 경로 설정
                                .queryParam("access-key", accessKey);  // 쿼리 파라미터 추가
                        params.forEach((key, value) -> {
                            Optional.ofNullable(value)
                                    .ifPresent(v -> uriBuilder.queryParam(key, v));
                        });
                        return uriBuilder.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(JobSearchResDto.class)
                    .block();// 동기 처리

            // 사람인 API Res check
            if (jobSearchResDto == null || jobSearchResDto.jobs().count() == 0 || jobSearchResDto.jobs().job() == null) {
                log.error("사람인 채용정보 API 응답이 올바르지 않습니다: 0개의 채용공고를 응답받았습니다.");
                // 필요시 사용자 정의 예외를 던질 수 있음
                throw new JobSearchException("사람인 API에서 0개의 채용공고 응답이 왔습니다.");
            }

//            long filteredCount = jobSearchResDto.jobs().job().stream()
//                    .map(Converter::toEntity) // toEntity로 변환
//                    .filter(jobPosting ->
//                            jobPosting.getExperienceMin() >= 0 && jobPosting.getExperienceMin() <= 2 && // experience_min 필터
//                                    jobPosting.getExperienceMax() >= 0 && jobPosting.getExperienceMax() <= 3)   // experience_max 필터
//                    .count(); // 필터링된 개수 계산
//
//            System.out.println("필터링된 JobPosting 개수: " + filteredCount);

            List<JobPosting> jobPostings = jobSearchResDto.jobs().job().stream()
                    .map(Converter::toEntity) // toEntity로 변환
                    .filter(jobPosting ->
                            jobPosting.getExperienceMin() >= 0 && jobPosting.getExperienceMin() <= 2 && // experience_min 필터
                                    jobPosting.getExperienceMax() >= 0 && jobPosting.getExperienceMax() <= 3)   // experience_max 필터
                    .toList(); // 필터링된 데이터를 리스트로 수집

            jobPostingRepository.saveAll(jobPostings);  // 여러 엔티티를 한 번에 저장
            log.info("success [searchJobs] page: {}", startPage);

            return jobSearchResDto;

        } catch (Exception e) {
            log.error("Error occurred during [searchJobs] ", e);
            throw e;
        }
    }

    @Override
    public List<JobPostingEmailDto> getFilteredJobPostingsByUserId(Long userId) {
        // 1. FilteredJobPosting 엔티티를 userId로 조회
        FilteredJobPosting filteredJobPosting = filteredJobPostingRepository.findByUserId(userId).orElse(null);

        if (filteredJobPosting == null || filteredJobPosting.getJobPostings().isEmpty()) {
            return null;  // filteredJobPosting이 없거나, 저장된 jobPostings가 비어 있을 경우
        }

        // 2. JobPosting IDs를 split()으로 분리하고 Long 타입으로 변환
        List<Long> jobPostingIds = Arrays.stream(filteredJobPosting.getJobPostings().split(","))
                .map(String::trim)           // 공백 제거
                .map(Long::valueOf)          // String -> Long 변환
                .limit(5)           // 최대 5개까지만 처리
                .toList();

        // 3. JobPosting 레포지토리에서 ID들로 JobPosting 목록 조회 (최대 5개)
        List<JobPosting> jobPostings = jobPostingRepository.findAllById(jobPostingIds);

        // 4. 엔티티 -> DTO 변환
        return jobPostings.stream()
                .map(Converter::toEmailDTO) // 엔티티 -> DTO 변환
                .toList();
    }
}