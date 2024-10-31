package com.example.demo.user.global.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "account_id", nullable = false, length = 30, unique = true)
    var accountId: String,

    @Column(name = "password", nullable = false, length = 255)
    var password: String,

    @Column(name = "email", nullable = false, length = 255)
    var email: String,

    @Column(name = "type", nullable = false, length = 10)
    var type: String,

    @Column(name = "role", nullable = false, length = 10)
    var role: String,

    @Column(name = "subscription", nullable = false, length = 10)
    var subscription: String,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) : BaseEntity() {

    // 기본 생성자 - JPA에서 사용
    constructor() : this(
        id = null,
        accountId = "",
        password = "",
        email = "",
        type = "",
        role = "",
        subscription = "",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
}