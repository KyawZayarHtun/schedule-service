package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.io.JobIdentifier;

public interface TriggerService {

    boolean doesTriggerExist(JobIdentifier jobIdentifier);

}
