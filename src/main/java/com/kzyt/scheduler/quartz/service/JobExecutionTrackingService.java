package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.entity.JobExecutionHistory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import java.time.LocalDateTime;
import java.util.List;

public interface JobExecutionTrackingService {

    void recordJobToBeExecuted(JobExecutionContext context);
    void recordJobWasExecuted(JobExecutionContext context);
    void recordJobExecutionVetoed(JobExecutionContext context);
    void recordTriggerMisfired(Trigger trigger);
    void recordJobExecutionFailed(JobExecutionContext context, JobExecutionException jobException);

    List<JobExecutionHistory> getJobExecutionHistory(String jobName, String jobGroup);
    List<JobExecutionHistory> getMisfiredJobs();
    List<JobExecutionHistory> getFailedJobs(String jobName, String jobGroup);
    List<JobExecutionHistory> getExecutionHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate);

}
