package com.kzyt.scheduler.quartz.service;

public interface JobService {

    void pauseJob(String name, String group);

    void resumeJob(String name, String group);

    void deleteJob(String name, String group);

}
