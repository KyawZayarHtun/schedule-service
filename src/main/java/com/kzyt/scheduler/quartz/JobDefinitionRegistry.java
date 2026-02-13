package com.kzyt.scheduler.quartz;


import com.kzyt.scheduler.quartz.exception.QuartzJobOrTriggerNotFoundException;
import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobDetailDto;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobDefinitionRegistry {

    private final List<DefinedJob<?>> definedJobs;
    private final Map<JobIdentifier, DefinedJob<?>> jobDefinitions = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Registering {} job definitions", definedJobs.size());
        definedJobs.forEach(definedJob -> {
            JobIdentifier jobIdentifier = definedJob.getJobIdentifier();

            if (jobDefinitions.containsKey(jobIdentifier)) {
                log.warn("Duplicate job definition found for {}. Overwriting.", jobIdentifier);
            }

            jobDefinitions.put(jobIdentifier, definedJob);
        });

        log.info("Total {} job definitions registered.", jobDefinitions.size());
    }

    public List<JobDetailDto> getJobs() {
        return jobDefinitions.entrySet().stream()
                .map(entry -> {
                    JobIdentifier identifier = entry.getKey();
                    DefinedJob<?> definedJob = entry.getValue();
                    return new JobDetailDto(
                            identifier.name(),
                            identifier.group(),
                            definedJob.getDescription(),
                            definedJob.getJobClass(),
                            definedJob.getExpectedJobDataParameters()
                    );
                })
                .toList();
    }

    public JobDetailDto getJobDetail(String name, String group) {
        JobIdentifier jobIdentifier = new JobIdentifier(name, group);
        DefinedJob<?> definedJob = jobDefinitions.get(jobIdentifier);

        if (definedJob == null) {
            throw new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' not found.");
        }

        return new JobDetailDto(
                definedJob.getJobIdentifier().name(),
                definedJob.getJobIdentifier().group(),
                definedJob.getDescription(),
                definedJob.getJobClass(),
                definedJob.getExpectedJobDataParameters()
        );
    }

    public Set<String> getAllJobGroups() {
        return jobDefinitions.keySet().stream()
                .map(JobIdentifier::group)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getAllJobNamesByGroup(String group) {
        return jobDefinitions.keySet().stream()
                .filter(identifier -> identifier.group().equalsIgnoreCase(group))
                .map(JobIdentifier::name)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getAllJobNames() {
        return definedJobs.stream().map(j -> j.getJobIdentifier().name()).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<JobDataParameter> getJobDataParameters(String name, String group) {
        JobIdentifier jobIdentifier = new JobIdentifier(name, group);
        DefinedJob<?> definedJob = jobDefinitions.get(jobIdentifier);

        if (definedJob == null) {
            throw new QuartzJobOrTriggerNotFoundException("Job with name '" + name + "' and group '" + group + "' not found.");
        }

        return definedJob.getExpectedJobDataParameters();
    }

}
