package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.jobposting.dto.JobPostingEmailDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;

import java.util.List;

public interface JobPostingService {

    JobSearchResDto searchJobs(int startIdx, String sevenDaysAgo);

    List<JobPostingEmailDto> getFilteredJobPostingsByUserId(Long userId);
}
