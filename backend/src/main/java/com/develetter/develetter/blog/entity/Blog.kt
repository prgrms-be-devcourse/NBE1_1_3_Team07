package com.develetter.develetter.blog.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import lombok.experimental.SuperBuilder

@Entity
@Table(name = "blog")
data class Blog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "title")
    var title: String? = null,

    @Column(name = "link", length = 1000)
    var link: String? = null,

    @Column(name = "snippet", columnDefinition = "TEXT")
    var snippet: String? = null,

    @Column(name = "image_url", length = 1000)
    var imageUrl: String? = null
) : BaseEntity()
