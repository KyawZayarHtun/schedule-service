package com.kzyt.scheduler.core.service;

import com.kzyt.scheduler.core.io.JobIdentifier;

public interface TriggerService {

    boolean doesTriggerExist(JobIdentifier jobIdentifier);

}
