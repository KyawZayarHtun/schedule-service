package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.exception.JobDeleteFailException;
import com.kzyt.scheduler.quartz.exception.QuartzJobNotFoundException;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.service.JobService;
import com.kzyt.scheduler.quartz.service.TriggerService;
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
    private final TriggerService triggerService;

    @Override
    public void pauseJob(String name, String group) {

        doesJobExist(name, group);

        try {
            scheduler.pauseJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            log.error("Failed to pause job: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not pause job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void resumeJob(String name, String group) {
        doesJobExist(name, group);

        try {
            scheduler.resumeJob(new JobKey(name, group));
        } catch (SchedulerException e) {
            log.error("Failed to resume job: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not resume job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void deleteJob(String name, String group) {

        doesJobExist(name, group);
        triggerService.doesTriggerExist(name, group);

        try {
            boolean isSuccess = scheduler.deleteJob(new JobKey(name, group));
            if (!isSuccess) {
                throw new JobDeleteFailException("can not delete job: " + name + " in group: " + group);
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not delete job: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void doesJobExist(String name, String group) {
        try {
            if (!scheduler.checkExists(new JobKey(name, group))) {
                throw new QuartzJobNotFoundException("Schedule with name '" + name + "' and group '" + group + "' does not exist.");
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not check existence of job: " + name + " in group: " + group, e);
        }
    }

}
