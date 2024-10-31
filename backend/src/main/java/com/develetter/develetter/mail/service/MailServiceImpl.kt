package com.develetter.develetter.mail.service

import com.develetter.develetter.mail.entity.Mail
import com.develetter.develetter.mail.repository.MailRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Service
open class MailServiceImpl(
    private val mailRepository: MailRepository
) : MailService {

    @Transactional
    override fun updateMailSendingCheck(id: Long) {
        val mail: Mail? = mailRepository.findById(id).orElse(null)
        if (mail != null) {
            mail.updateMailCheck()
            mailRepository.save(mail)
        } else {
            log.error("Could not update sending_check with id {}", id)
        }
    }

    @Transactional
    override fun updateMailDeleted(id: Long) {
        val mail: Mail? = mailRepository.findById(id).orElse(null)
        if (mail != null) {
            mail.updateMailDelete()
            mailRepository.save(mail)
        } else {
            log.error("Could not deleted with id {}", id)
        }
    }
}
