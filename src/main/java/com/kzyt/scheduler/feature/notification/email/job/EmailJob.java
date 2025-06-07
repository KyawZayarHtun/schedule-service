package com.kzyt.scheduler.feature.notification.email.job;

import com.kzyt.scheduler.feature.notification.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailJob implements Job, Serializable {

    @Serial
    private static final long serialVersionUID = 3599428403283472317L;

    private final EmailService emailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();

        // Accessing job data
        String subject = dataMap.getString("subject");
        String body = dataMap.getString("body");
        String toRecipientsString = dataMap.getString("toRecipients");
        String ccRecipientsString = dataMap.getString("ccRecipients");

        String jobId = jobExecutionContext.getJobDetail().getKey().getName();
        String jobGroup = jobExecutionContext.getJobDetail().getKey().getGroup();

        log.info("jobId: {}, jobGroup: {}", jobId, jobGroup);
        log.info("param subject: {}, body: {}, toRecipientsString: {}, ccRecipientsString: {}", subject, body, toRecipientsString, ccRecipientsString);

    }
}
