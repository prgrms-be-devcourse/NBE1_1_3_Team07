package com.develetter.develetter.jobposting.batch;

import com.develetter.develetter.jobposting.service.FilteredJobPostingCacheService;
import com.develetter.develetter.userfilter.entity.JobPostingKeyword;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.entity.QJobPosting;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobPostingBatch {

    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final FilteredJobPostingRepository filteredJobPostingRepository;
    private final UserFilterRepository userFilterRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManagerFactory emf;

    private static final int chunkSize = 100;

    @Autowired
    private FilteredJobPostingCacheService filteredJobPostingCacheService;


    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Job 시작: " + jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    System.out.println("Job 완료: " + jobExecution.getJobInstance().getJobName());
                    // 모든 사용자 ID에 대해 Redis 캐시된 데이터 일괄 저장
                    saveAllCachedJobPostingsToDB(jobExecution);
                } else {
                    System.err.println("Job 실패: " + jobExecution.getAllFailureExceptions());
                }
            }

            private void saveAllCachedJobPostingsToDB(JobExecution jobExecution) {
                // JobParameters에서 userIds 가져오기
                String userIdsParam = jobExecution.getJobParameters().getString("userIds");

                if (userIdsParam != null) {
                    List<Long> userIds = Arrays.stream(userIdsParam.split(","))
                            .map(Long::valueOf)
                            .toList();

                    for (Long userId : userIds) {
                        Set<Object> jobPostingIds = filteredJobPostingCacheService.getJobPostingsFromCache(userId);

                        // jobPostingIds를 String 타입으로 변환
                        String jobPostings = jobPostingIds.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(","));

                        // DB에 저장할 객체 생성 및 저장
                        FilteredJobPosting filteredJobPosting = FilteredJobPosting.builder()
                                .userId(userId)
                                .jobPostings(jobPostings) // ID들을 쉼표로 구분하여 저장
                                .build();
                        filteredJobPostingRepository.save(filteredJobPosting);

                        // Redis 캐시 삭제
                        filteredJobPostingCacheService.clearCache(userId);
                    }
                }
            }
        };
    }

    @Bean
    public Job filterJobPostingsJob(JPAQueryFactory jpaQueryFactory) {
        return new JobBuilder("filterJobPostingsJob", jobRepository)
                .listener(jobExecutionListener())
                .start(filterJobPostingsStep(jpaQueryFactory))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step filterJobPostingsStep(JPAQueryFactory jpaQueryFactory) {
        return new StepBuilder("filterJobPostingsStep", jobRepository)
                .<JobPosting, Long>chunk(chunkSize, platformTransactionManager)
                .reader(reader())
                .processor(itemProcessor(null))
                .writer(customItemWriter(null)) // 사용자 정의 writer 사용
                .build();
    }


//    @Bean
//    public RepositoryItemReader<JobPosting> reader() {
//        return new RepositoryItemReaderBuilder<JobPosting>()
//                .name("jobPostingItemReader")
//                .pageSize(chunkSize)
//                .repository(jobPostingRepository)
//                .methodName("findAll") // 모든 JobPosting을 조회
//                .sorts(Map.of("id", Sort.Direction.ASC))
//                .build();
//    }

    @Bean
    public QuerydslPagingItemReader<JobPosting> reader() {
        return new QuerydslPagingItemReader<>(emf, chunkSize, queryFactory -> queryFactory
                .selectFrom(QJobPosting.jobPosting));
    }

    @Bean
    @StepScope
    public ItemProcessor<JobPosting, Long> itemProcessor(
            @Value("#{jobParameters['userIds']}") String userIds) {

        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::valueOf)
                .toList();

        // 모든 UserFilter를 메모리에 로드
        List<UserFilter> userFilters = userFilterRepository.findAll();
        Map<Long, JobPostingKeyword> userFilterMap = userFilters.stream()
                .collect(Collectors.toMap(UserFilter::getUserId, UserFilter::getJobpostingKeywords));

        return jobPosting -> {
            for (Long userId : userIdList) {
                JobPostingKeyword jobpostingKeywords = userFilterMap.get(userId);

                // 필터링 조건을 통과하는지 확인
                if (jobpostingKeywords != null &&
                        containsIgnoreCase(jobPosting.getJobName(), parseKeywords(jobpostingKeywords.getJobNames())) &&
                        containsIgnoreCase(jobPosting.getLocationName(), parseKeywords(jobpostingKeywords.getLocationNames())) &&
                        containsIgnoreCase(jobPosting.getJobTypeName(), parseKeywords(jobpostingKeywords.getJobTypeNames())) &&
                        containsIgnoreCase(jobPosting.getIndustryName(), parseKeywords(jobpostingKeywords.getIndustryNames())) &&
                        containsIgnoreCase(jobPosting.getEducationLevelName(), parseKeywords(jobpostingKeywords.getEducationLevelNames()))) {

                    // 조건을 통과한 경우 JobPosting ID 반환
                    return jobPosting.getId(); // ID를 반환
                }
            }
            return null; // 필터 조건을 통과하지 못하면 null 반환
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Long> customItemWriter(@Value("#{jobParameters['userIds']}") String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::valueOf)
                .toList();

        return items -> {
            for (Long jobPostingId : items) {
                for (Long userId : userIdList) {
                    // 각 사용자별로 필터링된 잡 포스팅 ID를 Redis에 임시 저장
                    filteredJobPostingCacheService.addJobPostingToCache(userId, jobPostingId);
                }
            }
        };
    }


    private List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return List.of(); // 빈 리스트 반환
        }
        return List.of(keywords.split(","))
                .stream()
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    private boolean containsIgnoreCase(String fieldValue, List<String> keywords) {
        if (fieldValue == null) {
            return false;
        }
        return keywords.stream().anyMatch(keyword -> fieldValue.toLowerCase().contains(keyword.toLowerCase()));
    }
}
