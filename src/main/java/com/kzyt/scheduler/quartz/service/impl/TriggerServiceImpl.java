package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.exception.JobOrTriggerDeleteFailException;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.service.TriggerService;
import com.kzyt.scheduler.quartz.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerServiceImpl implements TriggerService {

    private final Scheduler scheduler;
    private final ValidationService validationService;

    @Override
    public void pauseTrigger(String name, String group) {
        validationService.doesTriggerExist(name, group);

        try {
            scheduler.pauseTrigger(new TriggerKey(name, group));
        } catch (Exception e) {
            log.error("Failed to pause trigger: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not pause trigger: " + name + " in group: " + group, e);
        }

    }

    @Override
    public void resumeTrigger(String name, String group) {
        validationService.doesTriggerExist(name, group);

        try {
            scheduler.resumeTrigger(new TriggerKey(name, group));
        } catch (Exception e) {
            log.error("Failed to resume trigger: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not resume trigger: " + name + " in group: " + group, e);
        }
    }

    @Override
    public void deleteTrigger(String name, String group) {

        validationService.doesTriggerExist(name, group);

        try {
            boolean isDeleted = scheduler.unscheduleJob(new TriggerKey(name, group));
            if (!isDeleted) {
                throw new JobOrTriggerDeleteFailException("can not delete trigger: " + name + " in group: " + group);
            }
        } catch (SchedulerException e) {
            log.error("Failed to delete trigger: {} in group: {}", name, group, e);
            throw new QuartzSchedulerException("can not delete trigger: " + name + " in group: " + group, e);
        }
    }

}
