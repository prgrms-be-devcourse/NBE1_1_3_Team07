package com.develetter.develetter.userfilter.repository

import com.develetter.develetter.userfilter.entity.UserFilter
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserFilterRepository : JpaRepository<UserFilter, Long> {
    fun findByUserId(userId: Long): Optional<UserFilter>
}
