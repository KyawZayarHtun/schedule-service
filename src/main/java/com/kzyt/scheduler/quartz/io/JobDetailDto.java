package com.kzyt.scheduler.quartz.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.quartz.Job;

import java.util.List;

public record JobDetailDto(
        String name,
        String group,
        String description,
        @JsonIgnore Class<? extends Job> jobClass,
        List<JobDataParameter> expectedJobDataParameters
) {
}
