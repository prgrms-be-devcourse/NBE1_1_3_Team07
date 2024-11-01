package com.develetter.develetter.user.provider

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.slf4j.LoggerFactory

@Component
class EmailProvider(
    private val javaMailSender: JavaMailSender,  // 이메일 전송을 위한 JavaMailSender 객체
    private val templateEngine: TemplateEngine   // Thymeleaf 템플릿 엔진
) {

    private val log = LoggerFactory.getLogger(EmailProvider::class.java)
    private val SUBJECT = "[develetter] 인증 메일"  // 이메일 제목

    /**
     * 인증 이메일을 전송하는 메서드.
     * 입력된 이메일 주소로 인증 번호가 포함된 HTML 이메일을 발송.
     * @param email 수신자의 이메일 주소
     * @param certificationNumber 인증 번호
     * @return 이메일 전송 성공 여부 (true: 성공, false: 실패)
     */
    fun sendVerificationEmail(email: String, certificationNumber: String): Boolean {
        return try {
            // MIME 타입의 이메일 메시지 생성
            val message: MimeMessage = javaMailSender.createMimeMessage()
            val messageHelper = MimeMessageHelper(message, true)

            // 인증 번호가 포함된 HTML 내용 생성
            val htmlContent = getCertificationMessage(certificationNumber)

            // 이메일 수신자, 제목 및 본문 설정
            messageHelper.setTo(email)
            messageHelper.setSubject(SUBJECT)
            messageHelper.setText(htmlContent, true)  // HTML 형식의 본문 설정

            // 이메일 전송
            javaMailSender.send(message)
            true  // 전송 성공 시 true 리턴
        } catch (e: Exception) {
            log.info("이메일 {} 로 전송 실패", email)
            log.info("Error: {}", e)
            false
        }
    }

    /**
     * 인증 메일 본문 HTML을 Thymeleaf 템플릿으로 생성하는 메서드.
     * @param certificationNumber 인증 번호
     * @return 렌더링된 HTML 문자열
     */
    private fun getCertificationMessage(certificationNumber: String): String {
        // Thymeleaf의 Context 객체에 변수를 설정
        val context = Context()
        context.setVariable("certificationNumber", certificationNumber)

        // 템플릿 엔진을 사용하여 HTML 파일을 렌더링하고 결과를 문자열로 반환
        return templateEngine.process("verification-email", context)
    }
}