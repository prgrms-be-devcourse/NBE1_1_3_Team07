package com.develetter.develetter.mail.entity;

import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.conference.dto.ConferenceRegisterDto;
import com.develetter.develetter.global.entity.BaseEntity;
import com.develetter.develetter.jobposting.entity.JobPosting;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "mail")
public class Mail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;  // User 엔티티와 1:1 외래키 설정

    @Column(name = "sending_check", nullable = false)
    private Boolean sendingCheck = false;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    public Mail(Long userId) {
        this.userId = userId;
        this.sendingCheck = false;
        this.deleted = false;
    }


    public void updateMailCheck() {
        this.sendingCheck = true;
    }

    public void updateMailDelete() {
        this.deleted = true;
    }
}
