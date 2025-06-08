package com.kzyt.scheduler.quartz.io;

public record CreateJobRequest(
        String name,
        String group
) {
}
