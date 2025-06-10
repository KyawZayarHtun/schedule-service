package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobDefinitionRegistry jobDefinitionRegistry;
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobDetailDto>> getAllJobs() {
        List<JobDetailDto> jobs = jobDefinitionRegistry.getJobs();

        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/groups")
    public ResponseEntity<Set<String>> getAllJobGroups() {
        Set<String> groups = jobDefinitionRegistry.getAllJobGroups();

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/names-by-group/{group}")
    public ResponseEntity<Set<String>> getJobNamesByGroup(@PathVariable String group) {
        Set<String> names = jobDefinitionRegistry.getAllJobNamesByGroup(group);

        return ResponseEntity.ok(names);
    }

    @GetMapping("/parameters/{group}/{name}")
    public ResponseEntity<List<JobDataParameter>> getJobParameters(@PathVariable String name, @PathVariable String group) {
        List<JobDataParameter> jobDataParameters = jobDefinitionRegistry.getJobDataParameters(name, group);

        return ResponseEntity.ok(jobDataParameters);
    }

    @PostMapping("pause")
    public ResponseEntity<String> pauseJob(
            @Validated @RequestBody JobIdentifier request
    ) {
        jobService.pauseJob(request.name(), request.group());

        return ResponseEntity.ok("Job paused successfully.");
    }

    @PostMapping("resume")
    public ResponseEntity<String> resumeJob(
            @Validated @RequestBody JobIdentifier request
    ) {
        jobService.resumeJob(request.name(), request.group());

        return ResponseEntity.ok("Job paused successfully.");
    }


    @DeleteMapping("delete/{group}")
    public ResponseEntity<String> deleteJob(@PathVariable String group, @RequestParam String name) {
        jobService.deleteJob(name, group);

        return ResponseEntity.ok("Job deleted successfully.");
    }


}
