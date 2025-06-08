package com.kzyt.scheduler.feature.notification.email.job;

import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.AbstractDefinedJob;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Component
public class EmailJobDefinition extends AbstractDefinedJob<EmailJob> implements Serializable {

    @Serial
    private static final long serialVersionUID = -429583093197965322L;

    protected EmailJobDefinition() {
        super(
                "EmailJob",
                "Notification",
                EmailJob.class,
                "Sends email notifications based on job parameters.",
                List.of(
                        JobDataParameter.builder()
                                .name("subject")
                                .type("String")
                                .description("The subject line of the email.")
                                .required(true)
                                .isArray(false)
                                .build(),
                        JobDataParameter.builder()
                                .name("body")
                                .type("String")
                                .description("The main content/body of the email.")
                                .required(true)
                                .isArray(false)
                                .build(),
                        JobDataParameter.builder()
                                .name("toRecipients")
                                .type("String")
                                .description("Comma-separated list of recipient email addresses (e.g., 'a@example.com,b@example.com').")
                                .required(true)
                                .isArray(true)
                                .build(),
                        JobDataParameter.builder()
                                .name("ccRecipients")
                                .type("String")
                                .description("Comma-separated list of CC recipient email addresses.")
                                .required(false)
                                .isArray(true)
                                .build()
                )
        );
    }
}
