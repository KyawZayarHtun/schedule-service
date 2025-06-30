package com.kzyt.scheduler.quartz.io;

import com.kzyt.scheduler.quartz.validationRule.ValidCron;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ScheduleRequest {

    @NotNull(message = "Job name cannot be null", groups = {Default.class})
    @NotBlank(message = "Job name cannot be blank", groups = {Default.class})
    private String jobName;

    @NotNull(message = "Job group cannot be null", groups = {Default.class})
    @NotBlank(message = "Job group cannot be blank", groups = {Default.class})
    private String jobGroup;

    @NotNull(message = "Trigger Name cannot be null", groups = {Default.class})
    @NotBlank(message = "Trigger Name cannot be blank", groups = {Default.class})
    private String triggerName;

    @NotNull(message = "Trigger Group cannot be null", groups = {OnSimpleTrigger.class})
    @FutureOrPresent(message = "Start time must be in the future or present", groups = {OnSimpleTrigger.class})
    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Min(value = 1, message = "Repeat count must be at least 1", groups = {OnSimpleTrigger.class})
    private Integer repeatCount;

    @NotNull(message = "Repeat interval seconds cannot be null", groups = {OnSimpleTrigger.class})
    @Min(value = 1, message = "Repeat interval seconds must be at least 1", groups = {OnSimpleTrigger.class})
    private Integer repeatIntervalInSeconds;

    @ValidCron(message = "Invalid cron expression", groups = {OnCronTrigger.class})
    private String cronExpression;

    private Map<String, String> triggerDataMap;


}
