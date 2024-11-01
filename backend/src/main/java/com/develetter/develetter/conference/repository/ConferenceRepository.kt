package com.develetter.develetter.conference.repository

import com.develetter.develetter.conference.entity.Conference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ConferenceRepository : JpaRepository<Conference, Long> {

    @Query("SELECT c FROM Conference c WHERE c.applyEndDate >= :start AND c.applyEndDate <= :end")
    fun findByDateRange(@Param("start") start: LocalDate, @Param("end") end: LocalDate): List<Conference>
}
