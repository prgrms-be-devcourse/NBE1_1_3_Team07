package com.develetter.develetter.jobposting.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.scheduler.JobPostingSchedulerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingSchedulerImpl jobPostingSchedulerImpl;
    private final JobLauncher jobLauncher;
    private final Job filterJobPostingsJob;

    @GetMapping("/testCall")
    public ApiResponseDto<JobSearchResDto> callJobSearchApi() {
        jobPostingSchedulerImpl.fetchJobPostings();

        return new ApiResponseDto<>(200, "성공");
    }

    @PostMapping("/runJob")
    public ApiResponseDto<Void> runJob(@RequestParam("userId") Long userId) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        // JobParameters 설정
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("userId", userId)  // userId를 파라미터로 전달
                .toJobParameters();

        // Job 실행
        JobExecution jobExecution = jobLauncher.run(filterJobPostingsJob, jobParameters);
        System.out.println("job start");

        // Job 실행 상태 확인 및 응답
        return new ApiResponseDto<>(200, "성공");

    }
}