package com.kzyt.scheduler.core.io;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
public class JobDataParameter implements Serializable {

    @Serial
    private static final long serialVersionUID = -6461951048035851366L;

    private String name;
    private String type; // e.g., "String", "Integer", "Boolean", "Double"
    private String description;
    private boolean required;
    private boolean isArray;

}
