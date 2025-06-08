package com.kzyt.scheduler.feature.notification.sms.job;

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
public class SmsJob implements Job, Serializable {

    @Serial
    private static final long serialVersionUID = -4926145116903824482L;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();

        // Accessing job data
        String phoneNo = dataMap.getString("phoneNo");

        String jobId = jobExecutionContext.getJobDetail().getKey().getName();
        String jobGroup = jobExecutionContext.getJobDetail().getKey().getGroup();

        log.info("jobId: {}, jobGroup: {}", jobId, jobGroup);
        log.info("param phoneNo: {}", phoneNo);
    }

}
