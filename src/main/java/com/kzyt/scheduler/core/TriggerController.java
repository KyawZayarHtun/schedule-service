package com.kzyt.scheduler.core;

import com.kzyt.scheduler.core.quartz.JobDefinitionRegistry;
import com.kzyt.scheduler.core.service.TriggerService;
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
