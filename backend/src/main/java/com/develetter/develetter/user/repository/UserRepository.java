package com.develetter.develetter.user.repository;

import com.develetter.develetter.user.global.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByAccountId(String accountId);
    UserEntity findByAccountId(String accountId);
    UserEntity findById(Long id);
}
