package com.develetter.develetter.mail.service

import com.develetter.develetter.blog.dto.BlogDto
import com.develetter.develetter.blog.service.InterestService
import com.develetter.develetter.jobposting.service.JobPostingService
import com.develetter.develetter.mail.entity.Mail
import com.develetter.develetter.user.service.UserService
import jakarta.mail.internet.MimeMessage
import mu.KotlinLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.time.*
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

private val log = KotlinLogging.logger {}

@Service
@EnableAsync
open class AsyncMailService(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
    private val mailService: MailService,
    private val blogService: InterestService,
    private val userService: UserService,
    private val jobPostingService: JobPostingService,
    private val jobPostingCalendarService: JobPostingCalendarService
) {

    @Async
    open// 메일 전송 메서드
    fun sendMail(mail: Mail, conferenceHtml: String) {
        val userId = mail.userId
        try {
            val email = userService.getEmailByUserId(userId)

            // 메일 컨텐츠 구성
            val mailContent = createMailContent(mail.userId, conferenceHtml)

            // 이메일 전송
            sendMimeMessage(email, mailContent)

            // 메일 발송 완료 체크
            mailService.updateMailSendingCheck(mail.id!!)
            log.info("Send Mail Success for User ID: ${mail.id}")

        } catch (e: Exception) {
            log.error("Failed Send Mail, Resend Mail for User ID: ${mail.id}")

            val email = userService.getEmailByUserId(userId)

            // 메일 컨텐츠 구성
            val mailContent = createMailContent(mail.userId, conferenceHtml)

            // 이메일 전송
            sendMimeMessage(email, mailContent)

            // 메일 발송 완료 체크
            mailService.updateMailSendingCheck(mail.id!!)
        }
    }

    private fun createMailContent(userId: Long, conferenceHtml: String): String {
        val jobPostingList = jobPostingService.getFilteredJobPostingsByUserId(userId)
        val blog = blogService.getBlogByUserId(userId)
        val jobPostingHtml = jobPostingCalendarService.createJobPostingCalendar(jobPostingList)
        val date = getWeekOfMonth(LocalDate.now())

        return setContext(date, jobPostingHtml.toString(), blog, conferenceHtml)
    }

    private fun sendMimeMessage(email: String, mailContent: String) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")
        mimeMessageHelper.setTo(email)
        mimeMessageHelper.setSubject("${getWeekOfMonth(LocalDate.now())} develetter 뉴스레터")
        mimeMessageHelper.setText(mailContent, true)
        javaMailSender.send(mimeMessage)
    }

    // 날짜 (ex. 9월 둘째주) 가져오는 메서드
    fun getWeekOfMonth(localDate: LocalDate): String {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        val weekFields = WeekFields.of(DayOfWeek.MONDAY, 4)
        val weekOfMonth = localDate.get(weekFields.weekOfMonth())

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            return getWeekOfMonth(localDate.minusDays(localDate.dayOfMonth.toLong()))
        }

        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
        val lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth())
        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) &&
            lastDayOfMonth.dayOfWeek < DayOfWeek.THURSDAY
        ) {
            return getWeekOfMonth(lastDayOfMonth.plusDays(1))
        }

        // 주차를 한국어로 변환
        val weekInKorean = arrayOf("첫", "둘", "셋", "넷", "다섯")[weekOfMonth - 1]
        return "${localDate.monthValue}월 $weekInKorean 째 주"
    }

    // thymeleaf를 통한 mail.html 적용
    fun setContext(date: String, jobPostingHtml: String, blogDto: BlogDto, conferenceHtml: String): String {
        val context = Context().apply {
            setVariable("date", date)
            setVariable("jobPostingHtml", jobPostingHtml)
            setVariable("blog", blogDto)
            setVariable("conferenceHtml", conferenceHtml)
        }
        return templateEngine.process("email", context)
    }
}
