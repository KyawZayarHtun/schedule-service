package com.kzyt.scheduler.core.service;

import com.kzyt.scheduler.core.io.JobIdentifier;

public interface JobService {

    boolean createJob(JobIdentifier jobIdentifier);

    boolean deleteJob(JobIdentifier jobIdentifier);

    boolean doesJobExist(JobIdentifier jobIdentifier);
}
