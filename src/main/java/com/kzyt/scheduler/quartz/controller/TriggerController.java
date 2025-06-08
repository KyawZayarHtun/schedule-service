package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.quartz.service.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("schedule")
@RequiredArgsConstructor
public class TriggerController {

    private final JobDefinitionRegistry jobDefinitionRegistry;
    private final TriggerService triggerService;




}
