package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.io.ScheduleRequest;

public interface ScheduleService {

    void createSimpleSchedule(ScheduleRequest request);

    void createCronSchedule(ScheduleRequest request);

}
