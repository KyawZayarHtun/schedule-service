package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.io.CreateScheduleRequest;
import com.kzyt.scheduler.quartz.io.OnCronTrigger;
import com.kzyt.scheduler.quartz.io.OnSimpleTrigger;
import com.kzyt.scheduler.quartz.service.ScheduleService;
import com.kzyt.scheduler.quartz.service.ValidationService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ValidationService validationService;

    @PostMapping("simple")
    public ResponseEntity<String> createSimpleSchedule(@Validated({Default.class, OnSimpleTrigger.class}) @RequestBody CreateScheduleRequest request) {
        validateScheduleRequest(request.getJobName(), request.getJobGroup(), request.getJobDataMap());

        scheduleService.createSimpleSchedule(request);

        return ResponseEntity.ok("Simple schedule created successfully.");
    }

    @PostMapping("cron")
    public ResponseEntity<String> createCronSchedule(@Validated({Default.class, OnCronTrigger.class}) @RequestBody CreateScheduleRequest request) {

        validateCronScheduleRequest(request.getJobName(), request.getJobGroup(), request.getJobDataMap(), request.getCronExpression());

        scheduleService.createCronSchedule(request);
        return ResponseEntity.ok("Cron schedule created successfully.");
    }

    private void validateScheduleRequest(String jobName, String jobGroup, Map<String, String> jobDataMap) {
        validationService.doesJobImplement(jobName, jobGroup);

        validationService.doesJobDataParametersMatch(jobName, jobGroup, jobDataMap);
    }

    private void validateCronScheduleRequest(String jobName, String jobGroup, Map<String, String> jobDataMap, String cronExpression) {
        validateScheduleRequest(jobName, jobGroup, jobDataMap);

        validationService.doesCronValid(cronExpression);
    }

}
