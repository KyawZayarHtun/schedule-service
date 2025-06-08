package com.kzyt.scheduler.quartz;

import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import org.quartz.Job;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDefinedJob<T extends Job> implements DefinedJob<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 5185907124013839259L;

    private final JobIdentifier jobIdentifier;
    private final Class<T> jobClass;
    private final String description;
    private final List<JobDataParameter> expectedJobDataParameters;

    protected AbstractDefinedJob(String name, String group, Class<T> jobClass, String description, List<JobDataParameter> expectedJobDataParameters) {
        this.jobIdentifier = new JobIdentifier(name, group);
        this.jobClass = jobClass;
        this.description = description;
        this.expectedJobDataParameters = Collections.unmodifiableList(expectedJobDataParameters);
    }

    @Override
    public JobIdentifier getJobIdentifier() {
        return jobIdentifier;
    }

    @Override
    public Class<T> getJobClass() {
        return jobClass;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<JobDataParameter> getExpectedJobDataParameters() {
        return expectedJobDataParameters;
    }
}
