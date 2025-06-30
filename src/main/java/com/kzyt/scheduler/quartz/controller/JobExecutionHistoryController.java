package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.entity.JobExecutionHistory;
import com.kzyt.scheduler.quartz.service.JobExecutionTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/job-execution")
@RequiredArgsConstructor
public class JobExecutionHistoryController {

    private final JobExecutionTrackingService trackingService;

    @GetMapping("/history/{jobName}/{jobGroup}")
    public ResponseEntity<List<JobExecutionHistory>> getJobHistory(
            @PathVariable String jobName,
            @PathVariable String jobGroup) {
        List<JobExecutionHistory> history = trackingService.getJobExecutionHistory(jobName, jobGroup);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/misfired")
    public ResponseEntity<List<JobExecutionHistory>> getMisfiredJobs() {
        List<JobExecutionHistory> misfiredJobs = trackingService.getMisfiredJobs();
        return ResponseEntity.ok(misfiredJobs);
    }

    @GetMapping("/failed/{jobName}/{jobGroup}")
    public ResponseEntity<List<JobExecutionHistory>> getFailedJobs(
            @PathVariable String jobName,
            @PathVariable String jobGroup) {
        List<JobExecutionHistory> failedJobs = trackingService.getFailedJobs(jobName, jobGroup);
        return ResponseEntity.ok(failedJobs);
    }

    @GetMapping("/history/range")
    public ResponseEntity<List<JobExecutionHistory>> getHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<JobExecutionHistory> history = trackingService.getExecutionHistoryByDateRange(startDate, endDate);
        return ResponseEntity.ok(history);
    }

}
