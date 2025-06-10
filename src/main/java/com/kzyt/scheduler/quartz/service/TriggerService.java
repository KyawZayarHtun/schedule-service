package com.kzyt.scheduler.quartz.service;

public interface TriggerService {

    void pauseTrigger(String name, String group);

    void resumeTrigger(String name, String group);

    void deleteTrigger(String name, String group);

}
