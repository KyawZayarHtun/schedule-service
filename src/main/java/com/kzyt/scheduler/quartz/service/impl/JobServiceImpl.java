package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.exception.JobOrTriggerDeleteFailException;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.service.JobService;
import com.kzyt.scheduler.quartz.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final Scheduler scheduler;
    private final ValidationService validationService;

    @Override
    public void pauseJob(String name, String group) {

        validationService.doesJobExist(name, group);

        try {
            scheduler.pauseJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            log.error("Failed to pause job: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not pause job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void resumeJob(String name, String group) {
        validationService.doesJobExist(name, group);

        try {
            scheduler.resumeJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            log.error("Failed to resume job: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not resume job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void deleteJob(String name, String group) {

        validationService.doesJobExist(name, group);
        validationService.doesAssociatedTriggersExist(name, group);

        try {
            boolean isDeleted = scheduler.deleteJob(new JobKey(name, group));
            if (!isDeleted) {
                throw new JobOrTriggerDeleteFailException("can not delete job: " + name + " in group: " + group);
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not delete job: " + name + " in group: " + group, e);
        }
    }

}
