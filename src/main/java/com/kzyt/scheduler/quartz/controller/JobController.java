package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import com.kzyt.scheduler.quartz.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management")
public class JobController {

    private final JobDefinitionRegistry jobDefinitionRegistry;
    private final JobService jobService;

    @Operation(summary = "Get all jobs",
            description = "Retrieve a list of all jobs in the scheduler")
    @ApiResponse(responseCode = "200", description = "List of jobs retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobDetailDto[].class)))
    @GetMapping
    public ResponseEntity<List<JobDetailDto>> getAllJobs() {
        List<JobDetailDto> jobs = jobDefinitionRegistry.getJobs();

        return ResponseEntity.ok(jobs);
    }

    @Operation(summary = "Get All Group of Jobs",
            description = "Retrieve a list of all jobs group in scheduler")
    @ApiResponse(responseCode = "200", description = "List of job groups retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String[].class)))
    @GetMapping("/groups")
    public ResponseEntity<Set<String>> getAllJobGroups() {
        Set<String> groups = jobDefinitionRegistry.getAllJobGroups();

        return ResponseEntity.ok(groups);
    }

    @Operation(summary = "Get job names by group",
            description = "Retrieve a list of job names for a specific job group")
    @ApiResponse(responseCode = "200", description = "List of job names by group retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String[].class)))
    @GetMapping("/names-by-group/{group}")
    public ResponseEntity<Set<String>> getJobNamesByGroup(@PathVariable String group) {
        Set<String> names = jobDefinitionRegistry.getAllJobNamesByGroup(group);

        return ResponseEntity.ok(names);
    }

    @Operation(summary = "Get job necessary parameters",
            description = "Retrieve a list of necessary parameters for a specific job")
    @ApiResponse(responseCode = "200", description = "List of job parameters retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = JobDataParameter[].class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @GetMapping("/parameters/{group}/{name}")
    public ResponseEntity<List<JobDataParameter>> getJobParameters(@PathVariable String name, @PathVariable String group) {
        List<JobDataParameter> jobDataParameters = jobDefinitionRegistry.getJobDataParameters(name, group);

        return ResponseEntity.ok(jobDataParameters);
    }

    @Operation(summary = "Pause a job",
            description = "Pause a specific job by its name and group")
    @ApiResponse(responseCode = "200", description = "Job paused successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("pause")
    public ResponseEntity<String> pauseJob(
            @Validated @RequestBody JobIdentifier request
    ) {
        jobService.pauseJob(request.name(), request.group());

        return ResponseEntity.ok("Job paused successfully.");
    }

    @Operation(summary = "Resume a job",
            description = "Resume a specific job by its name and group")
    @ApiResponse(responseCode = "200", description = "Job resume successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping("resume")
    public ResponseEntity<String> resumeJob(
            @Validated @RequestBody JobIdentifier request
    ) {
        jobService.resumeJob(request.name(), request.group());

        return ResponseEntity.ok("Job resume successfully.");
    }

    @Operation(summary = "Delete a job",
            description = "Delete a specific job by its name and group")
    @ApiResponse(responseCode = "200", description = "Job delete successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = void.class)))
    @ApiResponse(responseCode = "500", description = "An error occurred while processing the request. Please try again later.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Failed to delete job or trigger due to an internal server error.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "409", description = "Job with requested name and group has associated triggers. Please delete all associated triggers first.",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @DeleteMapping("delete/{group}")
    public ResponseEntity<String> deleteJob(@PathVariable String group, @RequestParam String name) {
        jobService.deleteJob(name, group);

        return ResponseEntity.ok("Job deleted successfully.");
    }


}
