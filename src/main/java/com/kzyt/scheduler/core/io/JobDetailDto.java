package com.kzyt.scheduler.core.io;

import java.util.List;

public record JobDetailDto(
        String name,
        String group,
        String description,
        List<JobDataParameter> expectedJobDataParameters
) {
}
