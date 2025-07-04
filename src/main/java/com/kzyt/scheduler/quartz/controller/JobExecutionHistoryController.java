package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.entity.JobExecutionHistory;
import com.kzyt.scheduler.quartz.service.JobExecutionTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("job-execution")
@RequiredArgsConstructor
@Tag(name = "Job Execution History")
public class JobExecutionHistoryController {

    private final JobExecutionTrackingService trackingService;

    @Operation(summary = "Retrieve Schedule History by Job Name and Group",
            description = "Get the execution history of a job by its name and group.")
    @ApiResponse(responseCode = "200", description = "List of job execution history retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobExecutionHistory[].class)))
    @GetMapping("/history/{jobName}/{jobGroup}")
    public ResponseEntity<List<JobExecutionHistory>> getJobHistory(
            @PathVariable String jobName,
            @PathVariable String jobGroup) {
        List<JobExecutionHistory> history = trackingService.getJobExecutionHistory(jobName, jobGroup);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Retrieve all schedule misfired records",
            description = "Get all misfired job execution records.")
    @ApiResponse(responseCode = "200", description = "List of misfired job execution history retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobExecutionHistory[].class)))
    @GetMapping("/misfired")
    public ResponseEntity<List<JobExecutionHistory>> getMisfiredJobs() {
        List<JobExecutionHistory> misfiredJobs = trackingService.getMisfiredJobs();
        return ResponseEntity.ok(misfiredJobs);
    }

    @Operation(summary = "Retrieve all failed schedule jobs",
            description = "Get all failed job execution records.")
    @ApiResponse(responseCode = "200", description = "List of failed job execution history retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobExecutionHistory[].class)))
    @GetMapping("/failed/{jobName}/{jobGroup}")
    public ResponseEntity<List<JobExecutionHistory>> getFailedJobs(
            @PathVariable String jobName,
            @PathVariable String jobGroup) {
        List<JobExecutionHistory> failedJobs = trackingService.getFailedJobs(jobName, jobGroup);
        return ResponseEntity.ok(failedJobs);
    }

    @Operation(summary = "Retrieve job execution history by date range",
            description = "Get the execution history of jobs within a specified date range.")
    @ApiResponse(responseCode = "200", description = "List of job execution history by date range retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobExecutionHistory[].class)))
    @GetMapping("/history/range")
    public ResponseEntity<List<JobExecutionHistory>> getHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<JobExecutionHistory> history = trackingService.getExecutionHistoryByDateRange(startDate, endDate);
        return ResponseEntity.ok(history);
    }

}
