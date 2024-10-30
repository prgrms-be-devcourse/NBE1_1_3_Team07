package com.develetter.develetter.mail.repository;

import com.develetter.develetter.mail.entity.Mail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MailRepository extends JpaRepository<Mail, Long> {
    Page<Mail> findByDeletedIsFalse(Pageable pageable);
}

