package com.develetter.develetter.conference.entity

import com.develetter.develetter.conference.dto.ConferenceRegisterDto
import com.develetter.develetter.global.entity.BaseEntity
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.time.LocalDate

@Entity
@Table(name = "conference")
class Conference(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "host", nullable = false)
    var host: String,

    @Column(name = "apply_start_date", nullable = false)
    var applyStartDate: LocalDate,

    @Column(name = "apply_end_date", nullable = false)
    var applyEndDate: LocalDate,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDate,

    @Column(name = "url", nullable = false)
    var url: String
) : BaseEntity() {

    fun updateConference(dto: ConferenceRegisterDto) {
        this.name = dto.name
        this.host = dto.host
        this.startDate = dto.startDate
        this.endDate = dto.endDate
        this.url = dto.url
    }
}
