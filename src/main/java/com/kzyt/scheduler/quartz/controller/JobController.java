package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.io.CreateJobRequest;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobDefinitionRegistry jobDefinitionRegistry;
    private final JobService jobService;

    @GetMapping("/groups")
    public ResponseEntity<Set<String>> getAllJobGroups() {
        Set<String> groups = jobDefinitionRegistry.getAllJobGroups();

        if (groups.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/names-by-group/{group}")
    public ResponseEntity<List<String>> getJobNamesByGroup(@PathVariable String group) {
        List<String> names = jobDefinitionRegistry.getJobIdentifiersByGroup(group).stream()
                .map(JobIdentifier::name)
                .collect(Collectors.toList());

        if (names.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(names);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createJob(@RequestBody CreateJobRequest request) {

        JobIdentifier jobIdentifier = new JobIdentifier(request.name(), request.group());

        boolean isSuccess = jobService.createJob(jobIdentifier);

        if (isSuccess) {
            return ResponseEntity.ok("Job '" + jobIdentifier + "' created/updated successfully.");
        } else {
            return ResponseEntity.badRequest()
                    .body("Failed to create/update job '" + jobIdentifier + "'. Make sure the job definition exists in the registry.");
        }

    }

    @GetMapping
    public ResponseEntity<List<JobDetailDto>> getAllJobs() {
        List<JobDetailDto> jobs = jobDefinitionRegistry.getJobs();

        if (jobs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobs);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteJob(@RequestBody JobIdentifier jobIdentifier) {
        boolean success = jobService.deleteJob(jobIdentifier);
        if (success) {
            return ResponseEntity.ok("Job " + jobIdentifier + " deleted.");
        } else {
            // Updated error message for clarity
            return ResponseEntity.badRequest().body("Failed to delete job " + jobIdentifier + ". It might not exist or has active triggers. Delete all triggers first.");
        }
    }



}
