package com.kzyt.scheduler.quartz.service;

public interface ValidationService {

    void doesJobExist(String name, String group);

    void doesTriggerExist(String name, String group);

    void doesAssociatedTriggersExist(String name, String group);
}
