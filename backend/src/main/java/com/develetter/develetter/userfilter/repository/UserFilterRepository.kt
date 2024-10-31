package com.develetter.develetter.userfilter.repository;

import com.develetter.develetter.userfilter.entity.UserFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFilterRepository extends JpaRepository<UserFilter, Long> {
    Optional<UserFilter> findByUserId(Long userId);
}
