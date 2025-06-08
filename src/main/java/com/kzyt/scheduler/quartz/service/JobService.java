package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.io.JobIdentifier;

public interface JobService {

    boolean createJob(JobIdentifier jobIdentifier);

    boolean deleteJob(JobIdentifier jobIdentifier);

    boolean doesJobExist(JobIdentifier jobIdentifier);
}
