package com.kzyt.scheduler.quartz.helper;

import com.kzyt.scheduler.quartz.io.ScheduleInfoDTO;
import com.kzyt.scheduler.quartz.io.ScheduleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobSchedulerHelper {

    private final Scheduler scheduler;

    public ScheduleInfoDTO buildScheduleInfoDTO(JobDetail jobDetail, Trigger trigger) throws SchedulerException {

        ScheduleInfoDTO.ScheduleInfoDTOBuilder builder = ScheduleInfoDTO.builder();

        builder.jobName(jobDetail.getKey().getName())
                .jobGroup(jobDetail.getKey().getGroup())
                .jobDescription(jobDetail.getDescription())
                .jobClass(jobDetail.getJobClass().getName())
                .jobDataMap(jobDetail.getJobDataMap().getWrappedMap());

        builder.triggerName(trigger.getKey().getName())
                .triggerGroup(trigger.getKey().getGroup())
                .triggerState(scheduler.getTriggerState(trigger.getKey()).name())
                .nextFireTime(trigger.getNextFireTime())
                .previousFireTime(trigger.getPreviousFireTime())
                .triggerDataMap(trigger.getJobDataMap().getWrappedMap());

        builder.startTime(trigger.getStartTime())
                .endTime(trigger.getEndTime());

        if (trigger instanceof SimpleTrigger simpleTrigger) {
            builder.triggerType("Simple")
                    .simpleTriggerRepeatInterval(simpleTrigger.getRepeatInterval())
                    .simpleTriggerRepeatCount(simpleTrigger.getRepeatCount());
        } else if (trigger instanceof CronTrigger cronTrigger) {
            builder.triggerType("Cron")
                    .cronExpression(cronTrigger.getCronExpression());
        }

        return builder.build();

    }

    public JobDetail createJobDetail(String jobName, String jobGroup, Class<? extends Job> jobClass) throws SchedulerException {

        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobName, jobGroup));

        if (jobDetail == null) {
            JobDetail jd = JobBuilder.newJob(jobClass)
                    .withIdentity(jobName, jobGroup)
                    .withDescription("Job for " + jobName + " in group " + jobGroup)
                    .storeDurably()
                    .build();

            scheduler.addJob(jd, true);

            return jd;
        }

        return jobDetail;
    }

    public void createSimpleTrigger(JobDetail jobDetail, ScheduleRequest request) throws SchedulerException {

        Date startAtDate = Date.from(request.getStartAt().atZone(ZoneId.systemDefault()).toInstant());

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withMisfireHandlingInstructionFireNow();

        configureSimpleScheduleBuilder(simpleScheduleBuilder, request.getRepeatCount(), request.getRepeatIntervalInSeconds());

        TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(request.getTriggerName(), request.getJobGroup())
                .startAt(startAtDate)
                .withSchedule(simpleScheduleBuilder)
                .usingJobData(new JobDataMap(request.getTriggerDataMap()))
                .withDescription("Trigger for " + request.getJobName() + " in group " + request.getJobGroup());

        addEndAtInTriggerIfExist(triggerBuilder, request.getEndAt());

        scheduleTrigger(triggerBuilder.build());
    }

    private void configureSimpleScheduleBuilder(SimpleScheduleBuilder simpleScheduleBuilder, Integer repeatCount, Integer repeatIntervalInSeconds) {
        if (repeatCount == null) {
            simpleScheduleBuilder.repeatForever();
        } else {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        }

        if (repeatIntervalInSeconds != null) {
            simpleScheduleBuilder.withIntervalInSeconds(repeatIntervalInSeconds);
        }
    }

    public void createCronTrigger(JobDetail jobDetail, ScheduleRequest request) throws SchedulerException {

        TriggerBuilder<CronTrigger> triggerBuilder = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(request.getTriggerName(), request.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(request.getCronExpression()))
                .usingJobData(new JobDataMap(request.getTriggerDataMap()))
                .withDescription("Cron Trigger for " + request.getJobName() + " in group " + request.getJobGroup());

        addEndAtInTriggerIfExist(triggerBuilder, request.getEndAt());

        scheduleTrigger(triggerBuilder.build());
    }

    private void addEndAtInTriggerIfExist(TriggerBuilder<?> triggerBuilder, LocalDateTime endAt) {
        Optional.ofNullable(endAt)
                .map(localDateTime -> Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .ifPresent(triggerBuilder::endAt);
    }

    private void scheduleTrigger(Trigger trigger) throws SchedulerException {
        TriggerKey triggerKey = trigger.getKey();

        if (scheduler.checkExists(triggerKey)) {
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("Rescheduled simple trigger '{}' for group '{}' to run at {}", triggerKey.getName(), triggerKey.getGroup(), trigger.getStartTime());
        } else {
            scheduler.scheduleJob(trigger);
            log.info("Scheduled simple trigger '{}' for group '{}' to run at {}", triggerKey.getName(), triggerKey.getGroup(), trigger.getStartTime());
        }

    }


}
