package com.kzyt.scheduler.feature.notification.sms.job;

import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.AbstractDefinedJob;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Component
public class SmsJobDefinition extends AbstractDefinedJob<SmsJob> implements Serializable {

    @Serial
    private static final long serialVersionUID = -4306798241868874889L;

    protected SmsJobDefinition() {
        super(
                "SmsJob",
                "Notification",
                SmsJob.class,
                "Sends SMS notifications based on job parameters.",
                List.of(
                        JobDataParameter.builder()
                                .name("phoneNo")
                                .description("The phone number to which the SMS will be sent.")
                                .required(true)
                                .isArray(false)
                                .build()
                )
        );
    }
}
