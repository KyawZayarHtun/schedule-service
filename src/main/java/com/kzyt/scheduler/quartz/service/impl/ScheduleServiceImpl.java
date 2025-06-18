package com.kzyt.scheduler.quartz.service.impl;

import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.exception.QuartzSchedulerException;
import com.kzyt.scheduler.quartz.helper.JobSchedulerHelper;
import com.kzyt.scheduler.quartz.io.ScheduleRequest;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.service.ScheduleService;
import com.kzyt.scheduler.quartz.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final Scheduler scheduler;
    private final ValidationService validationService;
    private final JobDefinitionRegistry registry;
    private final JobSchedulerHelper schedulerHelper;

    @Override
    public void createSimpleSchedule(ScheduleRequest request) {

        JobDetailDto jobInfo = registry.getJobDetail(request.getJobName(), request.getJobGroup());

        try {

            JobDetail jobDetail = schedulerHelper.createJobDetail(request.getJobName(), request.getJobGroup(), jobInfo.jobClass());

            schedulerHelper.createSimpleTrigger(jobDetail, request);

        } catch (SchedulerException e) {

            throw new QuartzSchedulerException("Can't create simple schedule for job: " + request.getJobName() + " in group: " + request.getJobGroup(), e);

        }

    }

    @Override
    public void createCronSchedule(ScheduleRequest request) {

        JobDetailDto jobInfo = registry.getJobDetail(request.getJobName(), request.getJobGroup());

        try {

            JobDetail jobDetail = schedulerHelper.createJobDetail(request.getJobName(), request.getJobGroup(), jobInfo.jobClass());

            schedulerHelper.createCronTrigger(jobDetail, request);


        } catch (SchedulerException e) {

            throw new QuartzSchedulerException("Can't create cron schedule for job: " + request.getJobName() + " in group: " + request.getJobGroup(), e);

        }

    }

}
