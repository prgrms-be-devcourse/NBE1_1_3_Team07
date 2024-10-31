package com.develetter.develetter.user.repository

import com.develetter.develetter.user.global.entity.CertificationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CertificationRepository : JpaRepository<CertificationEntity, String> {
    fun findByAccountId(accountId: String): CertificationEntity?
    fun deleteByAccountId(accountId: String)
    fun findTopByAccountIdOrderByCreatedAtDesc(accountId: String): CertificationEntity?
}