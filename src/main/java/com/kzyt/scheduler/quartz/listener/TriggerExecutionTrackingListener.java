package com.kzyt.scheduler.quartz.listener;

import com.kzyt.scheduler.quartz.service.JobExecutionTrackingService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TriggerExecutionTrackingListener implements TriggerListener {

    private final JobExecutionTrackingService trackingService;

    @Override
    public String getName() {
        return "TriggerExecutionTrackingListener";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        // Optional: Additional trigger fired tracking
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        trackingService.recordTriggerMisfired(trigger);
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
                                Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        // Optional: Additional trigger completion tracking
    }

}
