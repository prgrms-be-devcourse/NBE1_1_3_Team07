package com.develetter.develetter.mail.repository

import com.develetter.develetter.mail.entity.Mail
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MailRepository : JpaRepository<Mail, Long> {
    fun findByDeletedIsFalse(pageable: Pageable): Page<Mail>
}

