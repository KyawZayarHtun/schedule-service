package com.kzyt.scheduler.core.io;

public record CreateJobRequest(
        String name,
        String group
) {
}
