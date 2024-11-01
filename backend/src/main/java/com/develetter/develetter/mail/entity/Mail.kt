package com.develetter.develetter.mail.entity

import com.develetter.develetter.global.entity.BaseEntity
import com.querydsl.core.types.Projections.constructor
import jakarta.persistence.*

@Entity
@Table(name = "mail")
class Mail(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null, // ID는 기본값 null

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "sending_check", nullable = false)
    var sendingCheck: Boolean = false, // 기본값 false

    @Column(name = "deleted", nullable = false)
    var deleted: Boolean = false // 기본값 false

) : BaseEntity() {

    protected constructor() : this(0)

    // userId를 매개변수로 받는 생성자 추가
    constructor(userId: Long) : this(id = null, userId = userId)

    // 메일 전송 확인 상태 업데이트
    fun updateMailCheck() {
        sendingCheck = true
    }

    // 메일 삭제 상태 업데이트
    fun updateMailDelete() {
        deleted = true
    }
}
