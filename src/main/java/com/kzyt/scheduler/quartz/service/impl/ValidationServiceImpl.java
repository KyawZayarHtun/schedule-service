package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.exception.*;
import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final Scheduler scheduler;
    private final JobDefinitionRegistry registry;

    @Override
    public void doesJobExist(String name, String group) {
        try {
            if (!scheduler.checkExists(new JobKey(name, group))) {
                throw new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' does not exist.");
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not check existence of job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void doesJobImplement(String name, String group) {
        registry.getJobs().stream()
                .filter(job -> job.name().equals(name) && job.group().equals(group))
                .findFirst()
                .orElseThrow(() -> new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' does not implemented."));
    }

    @Override
    public void doesTriggerExist(String name, String group) {
        try {
            if (!scheduler.checkExists(new TriggerKey(name, group))) {
                throw new QuartzJobOrTriggerNotFoundException("Trigger with name '" + name + "' and group '" + group + "' does not exist.");
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not check existence of trigger: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void doesAssociatedTriggersExist(String name, String group) {
        try {
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(name, group));
            if (!triggersOfJob.isEmpty()) {
                throw new JobHasAssociatedTriggersException("Job with name '" + name + "' and group '" + group + "' has associated triggers. Please delete all associated triggers first.");
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not check existence of trigger: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void doesJobDataParametersMatch(String name, String group, Map<String, String> providedParameters) {

        JobDetailDto jobDetail = registry.getJobDetail(name, group);

        if (jobDetail == null) {
            throw new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' not found.");
        }

        if (jobDetail.expectedJobDataParameters() == null || jobDetail.expectedJobDataParameters().isEmpty()) {
            return; // No parameters expected, nothing to validate
        }

        if (providedParameters == null || providedParameters.isEmpty()) {
            throw new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' requires parameters but none were provided.");
        }

        // Validate that all expected parameters are present and of the correct type
        for (JobDataParameter expectedParameter : jobDetail.expectedJobDataParameters()) {
            final String parameterName = expectedParameter.getName();
            String  value = providedParameters.get(parameterName);

            if (expectedParameter.isRequired() && (value == null || value.isBlank())) {
                throw new MissingParameterException("Required parameter '" + parameterName + "' is missing for job: " + name + " in group: " + group);
            }

        }

    }

    @Override
    public void doesCronValid(String cronExpression) {
        boolean validExpression = CronExpression.isValidExpression(cronExpression);
        if (!validExpression) {
            throw new InvalidCronExpressionException("Cron expression '" + cronExpression + "' is not valid.");
        }
    }

}
