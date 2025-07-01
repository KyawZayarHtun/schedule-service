package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.io.*;
import com.kzyt.scheduler.quartz.service.ScheduleService;
import com.kzyt.scheduler.quartz.service.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("schedules")
@Tag(name = "Schedule Management")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ValidationService validationService;

    @Operation(summary = "Get all schedules",
            description = "Retrieve a list of all schedules in the scheduler")
    @ApiResponse(responseCode = "200", description = "List of schedules retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ScheduleInfoDTO[].class)))
    @GetMapping
    public ResponseEntity<List<ScheduleInfoDTO>> getAllScheduleJobs() {
        List<ScheduleInfoDTO> scheduleInfos = scheduleService.getAllScheduleJobs();

        return ResponseEntity.ok(scheduleInfos);
    }

    @Operation(summary = "Create a simple schedule",
            description = "Create a new simple schedule with the specified job name, group, and trigger data map")
    @ApiResponse(responseCode = "200", description = "Simple schedule created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "400", description = "Job related parameters not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @PostMapping("simple")
    public ResponseEntity<String> createSimpleSchedule(@Validated({Default.class, OnSimpleTrigger.class}) @RequestBody ScheduleRequest request) {
        validateScheduleRequest(request.getJobName(), request.getJobGroup(), request.getTriggerDataMap());

        scheduleService.createSimpleSchedule(request);

        return ResponseEntity.ok("Simple schedule created successfully.");
    }

    @Operation(summary = "Create a cron schedule",
            description = "Create a new cron schedule with the specified job name, group, trigger data map, and cron expression")
    @ApiResponse(responseCode = "200", description = "Cron schedule created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "400", description = "Job related parameters not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Invalid Cron Expression",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @PostMapping("cron")
    public ResponseEntity<String> createCronSchedule(@Validated({Default.class, OnCronTrigger.class}) @RequestBody ScheduleRequest request) {

        validateCronScheduleRequest(request.getJobName(), request.getJobGroup(), request.getTriggerDataMap(), request.getCronExpression());

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
