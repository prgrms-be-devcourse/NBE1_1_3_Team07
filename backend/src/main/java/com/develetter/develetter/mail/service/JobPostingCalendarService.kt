package com.develetter.develetter.mail.service

import com.develetter.develetter.jobposting.dto.JobPostingEmailDto
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

private val log = KotlinLogging.logger {}

@Service
@Transactional(readOnly = true)
open class JobPostingCalendarService {
    @Transactional
    open fun createJobPostingCalendar(jobPostingList: List<JobPostingEmailDto>): String? {
        try {
            return generateCalendarHtml(jobPostingList)
        } catch (e: Exception) {
            log.error("Error Create JobPosting Calendar", e)
        }
        return null
    }

    fun generateCalendarHtml(jobPostingList: List<JobPostingEmailDto>): String {
        val htmlContent = StringBuilder()

        htmlContent.append("<div style='max-width: 950px; margin: 0 auto; font-family: Arial, sans-serif;'>")
            .append("<div style= 'background-color: #004EA2; color: white; padding: 20px; text-align: center; font-size: large; border-radius: 10px 10px 0 0 ;'>")
            .append("<h2>채용 일정</h2></div>")
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
        for (week in 0..3) {
            htmlContent.append("<tr>")
            for (day in 0..6) {
                val currentDate = startDate.plusDays((week * 7 + day).toLong())
                var cellStyle = "padding: 10px; border: 1px solid #ddd; vertical-align: top; height: 120px; width: 14%;"

                if (currentDate == today) {
                    cellStyle += " background-color: #fffacd;"
                } else if (currentDate.isBefore(today)) {
                    cellStyle += " color: #ccc;"
                }

                htmlContent.append("<td style='").append(cellStyle).append("'>")
                    .append("<div style='font-weight: bold;'>")
                    .append(currentDate.monthValue).append("/")
                    .append(currentDate.dayOfMonth)
                    .append("</div>")

                // 채용 공고 이벤트 추가
                for (jobPosting in jobPostingList) {
                    if (isDateInRange(
                            currentDate,
                            jobPosting.postingDate.toLocalDate(),
                            jobPosting.expirationDate.toLocalDate()
                        )
                    ) {
                        htmlContent.append("<div style='background-color: #CCDEF0; margin: 2px 0; padding: 1px 2px; font-size: 11px; line-height: 1.2;'>")
                            .append(jobPosting.title).append(" | ").append(jobPosting.companyName)
                            .append("</div>")
                    }
                }

                htmlContent.append("</td>")
            }
            htmlContent.append("</tr>")
        }

        htmlContent.append("</table>")

        // 채용 공고 상세 정보 추가
        htmlContent.append("<div style='margin-top: 20px;'>")
            .append("<h2 style='color: #004EA2; font-size: medium'>채용 공고 상세 정보</h2>")

        for (jobPosting in jobPostingList) {
            htmlContent.append("<div style='margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 5px;'>")
                .append("<h4 style='margin: 0 0 10px 0; color: #004EA2'>")
                .append(jobPosting.title).append(" | ").append(jobPosting.companyName).append("</a></h4>")
                .append("<p style='margin: 5px 0;'>산업 이름: ").append(jobPosting.industryName).append("</p>")
                .append("<p style='margin: 5px 0;'>경력 요건: ").append(jobPosting.experienceName).append("</p>")
                .append("<p style='margin: 5px 0;'>직무 형태: ").append(jobPosting.jobTypeName).append("</p>")
                .append("<p style='margin: 5px 0;'>근무지: ").append(jobPosting.locationName).append("</p>")
                .append("<p style='margin: 5px 0;'>공고 기간: ").append(jobPosting.postingDate.toLocalDate()).append(" ~ ")
                .append(jobPosting.expirationDate.toLocalDate()).append("</p>")
                .append("<a href='").append(jobPosting.url)
                .append("' style='color: #004EA2; text-decoration: none;'>&rarr; 자세히 보기</a>")
                .append("</div>")
        }

        htmlContent.append("</div></div>")

        return htmlContent.toString()
    }

    private fun isDateInRange(date: LocalDate, start: LocalDate, end: LocalDate): Boolean {
        return !date.isBefore(start) && !date.isAfter(end)
    }
}