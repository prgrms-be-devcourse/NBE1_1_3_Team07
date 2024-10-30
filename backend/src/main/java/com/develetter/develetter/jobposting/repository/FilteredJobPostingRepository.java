package com.develetter.develetter.jobposting.repository;

import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilteredJobPostingRepository extends JpaRepository<FilteredJobPosting, Long> {
    Optional<FilteredJobPosting> findByUserId(long l);
}
