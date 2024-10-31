package com.develetter.develetter.user.global.entity

import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.DynamicInsert

@Entity(name = "certification")
@Table(name = "certification")
@DynamicInsert
class CertificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(name = "account_id", nullable = false, length = 30)
    var accountId: String,

    @Column(name = "email", nullable = false, length = 255)
    var email: String,

    @Column(name = "certification_number", nullable = false, length = 6)
    var certificationNumber: String
) : BaseEntity() {

    // 기본 생성자 제공 (필요시 추가 설정 가능)
    constructor() : this(
        id = 0,
        accountId = "",
        email = "",
        certificationNumber = ""
    )
}