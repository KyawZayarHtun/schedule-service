package com.kzyt.scheduler.quartz;

import com.kzyt.scheduler.quartz.io.JobDataParameter;
import com.kzyt.scheduler.quartz.io.JobIdentifier;
import org.quartz.Job;

import java.util.List;

public interface DefinedJob<T extends Job> {

    /**
     * Returns the unique identifier for this job definition.
     * @return JobIdentifier
     */
    JobIdentifier getJobIdentifier();

    /**
     * Returns the Quartz Job class associated with this definition.
     * @return Class<? extends Job>
     */
    Class<T> getJobClass();

    /**
     * Returns a human-readable description for this job.
     * @return String description
     */
    String getDescription();

    /**
     * Returns a required job params for this job.
     * @return List expectedJobDataParameters
     */
    List<JobDataParameter> getExpectedJobDataParameters();

}
