package com.develetter.develetter.mail.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
open class MailConfig {

    companion object {
        private const val MAIL_SMTP_AUTH = "mail.properties.mail.smtp.auth"
        private const val MAIL_DEBUG = "mail.properties.mail.smtp.debug"
        private const val MAIL_CONNECTION_TIMEOUT = "mail.properties.mail.smtp.connectiontimeout"
        private const val MAIL_SMTP_STARTTLS_ENABLE = "mail.properties.mail.smtp.starttls.enable"
        private const val MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required"
        private const val MAIL_PROTOCOL = "mail.protocol"
    }

    @Value("\${spring.mail.host}")
    private lateinit var host: String

    @Value("\${spring.mail.username}")
    private lateinit var username: String

    @Value("\${spring.mail.password}")
    private lateinit var password: String

    @Value("\${spring.mail.port}")
    private var port: Int = 0

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    private var auth: Boolean = false

    @Value("\${spring.mail.properties.mail.smtp.debug}")
    private var debug: Boolean = false

    @Value("\${spring.mail.properties.mail.smtp.connectiontimeout}")
    private var connectionTimeout: Int = 0

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    private var startTlsEnable: Boolean = false

    @Value("\${spring.mail.smtp.starttls.required}")
    private var startTlsRequired: Boolean = false

    @Value("\${spring.mail.protocol}")
    private lateinit var protocol: String

    @Bean
    open fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl().apply {
            host = this@MailConfig.host
            username = this@MailConfig.username
            password = this@MailConfig.password
            port = this@MailConfig.port

            javaMailProperties.apply {
                put(MAIL_SMTP_AUTH, auth)
                put(MAIL_DEBUG, debug)
                put(MAIL_CONNECTION_TIMEOUT, connectionTimeout)
                put(MAIL_SMTP_STARTTLS_ENABLE, startTlsEnable)
                put(MAIL_SMTP_STARTTLS_REQUIRED, startTlsRequired)
                put(MAIL_PROTOCOL, this@MailConfig.protocol)

                defaultEncoding = "UTF-8"
            }
        }
    }
}
