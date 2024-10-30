package com.develetter.develetter.mail.service;

import com.develetter.develetter.mail.entity.Mail;
import com.develetter.develetter.mail.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final MailRepository mailRepository;

    @Transactional
    @Override
    public void updateMailSendingCheck(Long id) {
        Mail mail = mailRepository.findById(id).orElse(null);
        if (mail != null) {
            mail.updateMailCheck();
            mailRepository.save(mail);
        } else {
            log.error("Could not update mail with id {}", id);
        }
    }

    @Transactional
    @Override
    public void updateMailDeleted(Long id) {
        Mail mail = mailRepository.findById(id).orElse(null);
        if (mail != null) {
            mail.updateMailDelete();
            mailRepository.save(mail);
        } else {
            log.error("Could not update mail with id {}", id);
        }
    }
}
