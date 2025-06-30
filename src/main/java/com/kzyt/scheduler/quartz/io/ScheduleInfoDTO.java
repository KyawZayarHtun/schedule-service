package com.kzyt.scheduler.quartz.io;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInfoDTO {

    private String jobGroup;
    private String jobName;
    private String jobDescription;
    private String jobClass;
    private Map<String, Object> jobDataMap; // From the JobDetail

    private String triggerGroup;
    private String triggerName;
    private String triggerState;
    private String triggerType; // Simple or Cron

    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime; // Trigger Start Time
    private Date endTime;   // Trigger End Time
    private Map<String, Object> triggerDataMap; // From the Trigger

    // Fields specific to SimpleTrigger
    private Integer simpleTriggerRepeatCount;
    private Long simpleTriggerRepeatInterval;

    // Fields specific to CronTrigger
    private String cronExpression;

}
