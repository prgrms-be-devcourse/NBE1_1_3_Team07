package com.develetter.develetter.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, String> {
    fun existsByAccountId(accountId: String): Boolean
    fun findByAccountId(accountId: String): UserEntity?
    fun findById(id: Long): UserEntity?
}