package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.service.TriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerServiceImpl implements TriggerService {

    private final Scheduler scheduler;

    @Override
    public boolean doesTriggerExist(JobIdentifier jobIdentifier) {

        try {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(new JobKey(jobIdentifier.name(), jobIdentifier.group()));
            return triggers != null && !triggers.isEmpty();
        } catch (SchedulerException e) {
            return false;
        }

    }

}
