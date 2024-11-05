package com.develetter.develetter.mail.service

interface MailService {
    fun updateMailSendingCheck(id: Long?)
    fun updateMailDeleted(id: Long?)
    fun isMailSent(id: Long?): Boolean
}

