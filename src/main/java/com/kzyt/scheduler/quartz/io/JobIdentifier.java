package com.kzyt.scheduler.quartz.io;

public record JobIdentifier(
    String name,
    String group
) {

    public JobIdentifier(String name, String group) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Job name cannot be null or empty.");
        }

        if (group == null || group.trim().isEmpty()) {
            throw new IllegalArgumentException("Job group cannot be null or empty.");
        }

        this.name = name;
        this.group = group;
    }
}
