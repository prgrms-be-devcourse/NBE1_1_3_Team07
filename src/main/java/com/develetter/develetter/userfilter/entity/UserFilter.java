package com.develetter.develetter.userfilter.entity;

import com.develetter.develetter.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_filter")
public class UserFilter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Embedded
    private JobPostingKeyword jobpostingKeywords;  // 임베디드 타입

    @Column(name = "blog_keywords", nullable = false, columnDefinition = "TEXT")
    private String blogKeywords;
}

