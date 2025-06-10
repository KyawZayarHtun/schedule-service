package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.exception.JobHasAssociatedTriggersException;
import com.kzyt.scheduler.quartz.exception.QuartzJobOrTriggerNotFoundException;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final Scheduler scheduler;

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

}
