package com.develetter.develetter.jobposting.scheduler;

import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.develetter.develetter.jobposting.service.JobPostingService;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobPostingSchedulerImpl implements JobPostingScheduler {

    @Value("${api.saramin.baseurl}")
    private String apiURL;

    private final JobPostingService jobPostingService;
    private final UserFilterRepository userFilterRepository;
    private final FilteredJobPostingRepository filteredJobPostingRepository;
    private final JobLauncher jobLauncher;
    private final Job filterJobPostingsJob;

    @Override
    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00시 정각에 실행
    @Transactional
    public void fetchJobPostings() {

        int startPage = 0;

        String twoWeeksAgo = LocalDateTime.now()
                .minusDays(14)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        while (true) {
            JobSearchResDto jobSearchResDto = jobPostingService.searchJobs(startPage++, twoWeeksAgo);

            if (jobSearchResDto.jobs().count() < 100) break;
        }
    }

//    @Scheduled(cron = "*/30 * * * * *") // 매 30초마다 실행 // TODO 추후에 수정 예정
    public void runBatchForAllUsers() {
        // 모든 필터링된 잡 포스팅 삭제
        filteredJobPostingRepository.deleteAll();

        List<UserFilter> userFilters = userFilterRepository.findAll(); // 모든 UserFilter 조회
        List<Long> userIds = userFilters.stream()
                .map(UserFilter::getUserId)
                .collect(Collectors.toList());

        // JobParameters 설정
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("userIds", String.join(",", userIds.stream().map(String::valueOf).collect(Collectors.toList()))) // 모든 userId를 문자열로 변환
                .addString("time", String.valueOf(System.currentTimeMillis())) // 현재 시간 추가
                .toJobParameters();

        // Job 실행
        try {
            jobLauncher.run(filterJobPostingsJob, jobParameters);
            System.out.println("Job started for userIds: " + userIds);
        } catch (Exception e) {
            System.err.println("Failed to run job due to: " + e.getMessage());
        }
    }
}
