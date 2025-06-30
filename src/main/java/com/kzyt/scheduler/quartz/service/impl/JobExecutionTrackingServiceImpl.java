package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.entity.JobExecutionHistory;
import com.kzyt.scheduler.quartz.io.JobExecutionStatus;
import com.kzyt.scheduler.quartz.repo.JobExecutionHistoryRepository;
import com.kzyt.scheduler.quartz.service.JobExecutionTrackingService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class JobExecutionTrackingServiceImpl implements JobExecutionTrackingService {

    private final JobExecutionHistoryRepository repository;

    private final Map<String, JobExecutionHistory> activeExecutions = new ConcurrentHashMap<>();

    @Override
    public void recordJobToBeExecuted(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        Trigger trigger = context.getTrigger();

        JobExecutionHistory history = new JobExecutionHistory(
                jobDetail.getKey().getName(),
                jobDetail.getKey().getGroup(),
                trigger.getKey().getName(),
                trigger.getKey().getGroup()
        );

        history.setScheduledFireTime(context.getScheduledFireTime());
        history.setActualFireTime(context.getFireTime());
        history.setExecutionStatus(JobExecutionStatus.SCHEDULED);
        history.setNextFireTime(context.getNextFireTime());

        history = repository.save(history);
        activeExecutions.put(getExecutionKey(context), history);
    }

    @Override
    public void recordJobWasExecuted(JobExecutionContext context) {
        String executionKey = getExecutionKey(context);
        JobExecutionHistory history = activeExecutions.get(executionKey);

        if (history != null) {
            history.setJobStartTime(context.getFireTime());
            history.setJobEndTime(new Date());
            history.setExecutionDurationMs(context.getJobRunTime());
            history.setExecutionStatus(JobExecutionStatus.COMPLETED);
            history.setNextFireTime(context.getNextFireTime());

            repository.save(history);
            activeExecutions.remove(executionKey);
        }
    }

    @Override
    public void recordJobExecutionVetoed(JobExecutionContext context) {
        String executionKey = getExecutionKey(context);
        JobExecutionHistory history = activeExecutions.get(executionKey);

        if (history != null) {
            history.setExecutionStatus(JobExecutionStatus.VETOED);
            repository.save(history);
            activeExecutions.remove(executionKey);
        }
    }

    @Override
    public void recordTriggerMisfired(Trigger trigger) {
        JobExecutionHistory history = new JobExecutionHistory(
                trigger.getJobKey().getName(),
                trigger.getJobKey().getGroup(),
                trigger.getKey().getName(),
                trigger.getKey().getGroup()
        );

        history.setScheduledFireTime(trigger.getNextFireTime());
        history.setExecutionStatus(JobExecutionStatus.MISFIRED);
        history.setIsMisfire(true);

        repository.save(history);
    }

    @Override
    public void recordJobExecutionFailed(JobExecutionContext context, JobExecutionException jobException) {
        String executionKey = getExecutionKey(context);
        JobExecutionHistory history = activeExecutions.get(executionKey);

        if (history != null) {
            history.setJobStartTime(context.getFireTime());
            history.setJobEndTime(new Date());
            history.setExecutionDurationMs(context.getJobRunTime());
            history.setExecutionStatus(JobExecutionStatus.FAILED);
            history.setErrorMessage(jobException.getMessage());

            repository.save(history);
            activeExecutions.remove(executionKey);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionHistory> getJobExecutionHistory(String jobName, String jobGroup) {
        return repository.findByJobNameAndJobGroupOrderByCreatedAtDesc(jobName, jobGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionHistory> getMisfiredJobs() {
        return repository.findByIsMisfireTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionHistory> getFailedJobs(String jobName, String jobGroup) {
        return repository.findFailedExecutions(jobName, jobGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobExecutionHistory> getExecutionHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByDateRange(startDate, endDate);
    }

    private String getExecutionKey(JobExecutionContext context) {
        return context.getJobDetail().getKey().toString() + "_" + context.getFireInstanceId();
    }

}
