package com.develetter.develetter.blog.entity;

import com.develetter.develetter.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blog")
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "link", length = 1000)
    private String link;

    @Column(name = "snippet", columnDefinition = "TEXT")
    private String snippet;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;
}
