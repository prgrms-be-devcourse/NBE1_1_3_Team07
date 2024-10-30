package com.develetter.develetter.jobposting.entity;

import com.develetter.develetter.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "filtered_job_posting")
public class FilteredJobPosting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "job_postings", nullable = false)
    private String jobPostings;

    // jobPostingList에 새로운 JobPosting ID를 추가하는 메서드
    public void addJobPosting(Long jobPostingId) {
        if (this.jobPostings == null || this.jobPostings.isEmpty()) {
            this.jobPostings = String.valueOf(jobPostingId);
        } else {
            this.jobPostings += "," + jobPostingId;
        }
    }

}
