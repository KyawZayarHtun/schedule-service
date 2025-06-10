package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.exception.JobHasAssociatedTriggersException;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.service.TriggerService;
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

    @Override
    public void doesTriggerExist(String name, String group) {
        try {
            if (scheduler.checkExists(new TriggerKey(name, group))) {
                throw new JobHasAssociatedTriggersException("Job with name '" + name + "' and group '" + group + "' has associated triggers. Please delete all associated triggers first.");
            }
        } catch (SchedulerException e) {
            throw new QuartzSchedulerException("can not check existence of trigger: " + name + " in group: " + group, e);
        }
    }

}
