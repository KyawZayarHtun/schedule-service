package com.kzyt.scheduler.feature.notification.sms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService{

    @Override
    public void sendSms(String phoneNo) {
        log.info("Sending Sms to {} ...", phoneNo);
    }

}
