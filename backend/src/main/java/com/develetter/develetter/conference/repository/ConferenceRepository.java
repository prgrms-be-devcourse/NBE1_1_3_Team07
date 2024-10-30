package com.develetter.develetter.conference.repository;

import com.develetter.develetter.conference.entity.Conference;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @Query("SELECT c FROM Conference c WHERE c.applyEndDate >= :start AND c.applyEndDate <= :end")
    List<Conference> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
