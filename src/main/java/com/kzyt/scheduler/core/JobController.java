package com.kzyt.scheduler.core;

import com.kzyt.scheduler.core.io.JobDataParameter;
import com.kzyt.scheduler.core.io.JobIdentifier;
import com.kzyt.scheduler.core.quartz.JobDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobDefinitionRegistry jobDefinitionRegistry;

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

    @GetMapping("/parameters/{group}/{name}")
    public ResponseEntity<List<JobDataParameter>> getJobParameters(
            @PathVariable String name,
            @PathVariable String group) {
        JobIdentifier jobIdentifier = new JobIdentifier(name, group);
        List<JobDataParameter> parameters = jobDefinitionRegistry.getJobDataParameters(jobIdentifier);
        if (parameters.isEmpty() && jobDefinitionRegistry.getJobDefinition(jobIdentifier).isEmpty()) {
            return ResponseEntity.notFound().build(); // Job definition itself not found
        }
        return ResponseEntity.ok(parameters);
    }



}
