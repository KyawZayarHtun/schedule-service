package com.kzyt.scheduler.quartz.service;

import java.util.Map;

public interface ValidationService {

    void doesJobExist(String name, String group);

    void doesJobImplement(String name, String group);

    void doesTriggerExist(String name, String group);

    void doesAssociatedTriggersExist(String name, String group);

    void doesJobDataParametersMatch(String name, String group, Map<String, String> parameters);

    void doesCronValid(String cronExpression);
}
