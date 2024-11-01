package com.develetter.develetter.mail.service

import com.develetter.develetter.conference.dto.ConferenceResDto
import com.develetter.develetter.conference.service.ConferenceService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
open class ConferenceCalendarService(
    private val conferenceService: ConferenceService
) {

    @Transactional
    open fun createConferenceCalendar(): String? {
        return try {
            val today = LocalDate.now()
            val conferenceList = conferenceService.getAllConferenceWithDateRange(today, today.plusMonths(1))
            log.info("Success Create Conference Calendar")
            generateCalendarHtml(conferenceList)
        } catch (e: Exception) {
            log.error("Error Create Conference Calendar", e)
            null
        }
    }

    private fun generateCalendarHtml(conferenceList: List<ConferenceResDto>): String {
        val htmlContent = StringBuilder()

        htmlContent.append("<div style='max-width: 950px; margin: 0 auto; font-family: Arial, sans-serif;'>")
            .append("<div style='background-color: #553830; color: white; padding: 20px; text-align: center; font-size: large; border-radius: 10px 10px 0 0;'>")
            .append("<h2>컨퍼런스 신청 일정</h2></div>")
            .append("<table style='width: 100%; border-collapse: collapse;'>")
            .append("<tr style='background-color: #f2f2f2;'>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>월</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>화</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>수</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>목</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>금</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>토</th>")
            .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>일</th>")
            .append("</tr>")

        // 현재 날짜 설정
        val today = LocalDate.now()
        // 첫 주의 시작일 조정
        val firstDayOfWeek = today.dayOfWeek
        val startDate = today.minusDays((firstDayOfWeek.value - 1).toLong())

        // 4주 동안의 달력 생성
        for (week in 0 until 4) {
            htmlContent.append("<tr>")
            for (day in 0 until 7) {
                val currentDate = startDate.plusDays((week * 7 + day).toLong())
                var cellStyle = "padding: 10px; border: 1px solid #ddd; vertical-align: top; height: 80px; width: 14%;"

                if (currentDate == today) {
                    cellStyle += " background-color: #fffacd;"
                } else if (currentDate.isBefore(today)) {
                    cellStyle += " color: #ccc;"
                }

                htmlContent.append("<td style='$cellStyle'>")
                    .append("<div style='font-weight: bold;'>")
                    .append("${currentDate.monthValue}/")
                    .append("${currentDate.dayOfMonth}")
                    .append("</div>")

                // 컨퍼런스 이벤트 추가
                for (conference in conferenceList) {
                    if (isDateInRange(currentDate, conference.applyStartDate, conference.applyEndDate)) {
                        htmlContent.append("<div style='background-color: #ebddcc; margin: 2px 0; padding: 1px 2px; font-size: 11px; line-height: 1.2;'>")
                            .append(conference.name)
                            .append("</div>")
                    }
                }

                htmlContent.append("</td>")
            }
            htmlContent.append("</tr>")
        }

        htmlContent.append("</table>")

        // 컨퍼런스 상세 정보 추가
        htmlContent.append("<div style='margin-top: 20px;'>")
            .append("<h2 style='color: #553830; font-size: medium'>컨퍼런스 상세 정보</h2>")

        for (conference in conferenceList) {
            htmlContent.append("<div style='margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 5px;'>")
                .append("<h4 style='margin: 0 0 10px 0; color: #553830'>")
                .append(conference.name).append(" | ").append(conference.host).append("</h4>")
                .append("<p style='margin: 5px 0;'>신청 기간: ").append(conference.applyStartDate).append(" ~ ").append(conference.applyEndDate).append("</p>")
                .append("<p style='margin: 5px 0;'>진행 기간: ").append(conference.startDate).append(" ~ ").append(conference.endDate).append("</p>")
                .append("<a href='").append(conference.url).append("' style='color: #553830; text-decoration: none;'>&rarr; 자세히 보기</a>")
                .append("</div>")
        }

        htmlContent.append("</div></div>")

        return htmlContent.toString()
    }

    private fun isDateInRange(date: LocalDate, start: LocalDate, end: LocalDate): Boolean {
        return !(date.isBefore(start) || date.isAfter(end))
    }
}
