package com.kzyt.scheduler.feature.notification.email.service;

public interface EmailService {

    void sendEmail(String recipient, String subject, String body);

}
