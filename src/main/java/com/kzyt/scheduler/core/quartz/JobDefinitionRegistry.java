package com.kzyt.scheduler.core.quartz;


import com.kzyt.scheduler.core.io.JobDetailDto;
import com.kzyt.scheduler.core.io.JobIdentifier;
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

    public Optional<DefinedJob<?>> getJobDefinition(JobIdentifier jobIdentifier) {
        return Optional.ofNullable(jobDefinitions.get(jobIdentifier));
    }

    public Set<String> getAllJobGroups() {
        return jobDefinitions.keySet().stream()
                .map(JobIdentifier::group)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<JobIdentifier> getJobIdentifiersByGroup(String group) {
        return jobDefinitions.keySet().stream()
                .filter(identifier -> identifier.group().equalsIgnoreCase(group))
                .toList();
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
                            definedJob.getExpectedJobDataParameters()
                    );
                })
                .toList();
    }

}
