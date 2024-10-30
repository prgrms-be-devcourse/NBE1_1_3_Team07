package com.develetter.develetter.user.repository;

import com.develetter.develetter.user.global.entity.CertificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<CertificationEntity, String> {
    CertificationEntity findByAccountId(String accountId);
    void deleteByAccountId(String accountId);
    CertificationEntity findTopByAccountIdOrderByCreatedAtDesc(String accountId);

}
