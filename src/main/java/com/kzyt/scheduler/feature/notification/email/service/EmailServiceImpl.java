package com.kzyt.scheduler.feature.notification.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        log.info("Sending email to: {}, Subject: {}, Body: {}....", recipient, subject, body);
    }


}
