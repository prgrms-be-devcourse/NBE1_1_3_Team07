package com.develetter.develetter.blog.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.experimental.SuperBuilder

@Entity
@Table(name = "filtered_blog")
data class FilteredBlog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long? = null,

    @Column(name = "blog", nullable = false)
    var blog: Long? = null
) : BaseEntity()
