package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.io.CreateScheduleRequest;

public interface ScheduleService {

    void createSimpleSchedule(CreateScheduleRequest request);

    void createCronSchedule(CreateScheduleRequest request);

}
