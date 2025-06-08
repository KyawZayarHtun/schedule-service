package com.kzyt.scheduler.core.service.impl;

import com.kzyt.scheduler.core.io.JobIdentifier;
import com.kzyt.scheduler.core.quartz.DefinedJob;
import com.kzyt.scheduler.core.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.core.service.JobService;
import com.kzyt.scheduler.core.service.TriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final Scheduler scheduler;
    private final TriggerService triggerService;
    private final JobDefinitionRegistry jobDefinitionRegistry;

    @Override
    public boolean createJob(JobIdentifier jobIdentifier) {

        var definedJobOpt = jobDefinitionRegistry.getJobDefinition(jobIdentifier);
        if (definedJobOpt.isEmpty()) {
            log.error("Job definition not found in registry for identifier: {}", jobIdentifier);
            return false;
        }
        DefinedJob<?> definedJob = definedJobOpt.get();

        try {
            // Build the JobDetailDto. The 'true' in addJob will replace if already present.
            // This is useful for updating the job's description or static JobDataMap.
            JobDetail jobDetail = buildJobDetail(jobIdentifier, definedJob.getJobClass(), new JobDataMap());
            scheduler.addJob(jobDetail, true); // addJob(jobDetail, replace): Stores the JobDetailDto, replacing if already present
            log.info("Job '{}' created/updated successfully in scheduler.", jobIdentifier);
            return true;
        } catch (SchedulerException e) {
            log.error("Error creating/updating job '{}': {}", jobIdentifier, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean deleteJob(JobIdentifier jobIdentifier) {
        try {

            // Check if the job exists
            if (!doesJobExist(jobIdentifier)) {
                log.warn("Job '{}' not found. Cannot delete a non-existent job.", jobIdentifier);
                return false;
            }

            // Check if the job has any triggers
            if (!triggerService.doesTriggerExist(jobIdentifier)) {
                log.warn("Job '{}' cannot be deleted because it has active triggers. Please delete all associated triggers first.", jobIdentifier);
                return false;
            }

            boolean deleted = scheduler.deleteJob(new JobKey(jobIdentifier.name(), jobIdentifier.group()));
            if (deleted) {
                log.info("Deleted job: {}", jobIdentifier);
            } else {
                log.warn("Job '{}' could not be deleted for an unknown reason.", jobIdentifier);
            }
            return deleted;
        } catch (SchedulerException e) {
            log.error("Error deleting job '{}': {}", jobIdentifier, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean doesJobExist(JobIdentifier jobIdentifier) {
        try {
            return scheduler.checkExists(new JobKey(jobIdentifier.name(), jobIdentifier.group()));
        } catch (SchedulerException e) {
            log.error("Error checking existence of job '{}': {}", jobIdentifier, e.getMessage(), e);
            return false;
        }
    }

    private JobDetail buildJobDetail(JobIdentifier jobIdentifier, Class<? extends Job> jobClass, JobDataMap jobData) {
        JobBuilder jobBuilder = JobBuilder.newJob(jobClass)
                .withIdentity(jobIdentifier.name(), jobIdentifier.group())
                .withDescription("Job: " + jobIdentifier.name() + " in group: " + jobIdentifier.group())
                .storeDurably(); // Store the job even if it has no triggers;

        if (jobData != null && !jobData.isEmpty()) {
            jobBuilder.usingJobData(jobData);
        }

        return jobBuilder.build();
    }


}
