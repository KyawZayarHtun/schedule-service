package com.kzyt.scheduler.quartz.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobIdentifier(
        @NotNull(message = "Name cannot be null")
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotNull(message = "Group cannot be null")
        @NotBlank(message = "Group cannot be blank")
        String group
) {

    public JobIdentifier(String name, String group) {
        this.name = name;
        this.group = group;
    }
}
