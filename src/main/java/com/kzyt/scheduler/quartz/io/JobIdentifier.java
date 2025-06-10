package com.kzyt.scheduler.quartz.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobIdentifier(
        @NotNull(message = "Job name cannot be null")
        @NotBlank(message = "Job name cannot be blank")
        String name,

        @NotNull(message = "Job group cannot be null")
        @NotBlank(message = "Job group cannot be blank")
        String group
) {

    public JobIdentifier(String name, String group) {
        this.name = name;
        this.group = group;
    }
}
