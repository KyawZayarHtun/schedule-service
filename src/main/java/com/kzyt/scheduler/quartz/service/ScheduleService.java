package com.kzyt.scheduler.quartz.service;

import com.kzyt.scheduler.quartz.io.ScheduleInfoDTO;
import com.kzyt.scheduler.quartz.io.ScheduleRequest;

import java.util.List;

public interface ScheduleService {

    List<ScheduleInfoDTO> getAllScheduleJobs();

    void createSimpleSchedule(ScheduleRequest request);

    void createCronSchedule(ScheduleRequest request);

}
