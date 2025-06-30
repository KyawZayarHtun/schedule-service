package com.kzyt.scheduler.quartz.listener;

import com.kzyt.scheduler.quartz.service.JobExecutionTrackingService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.listeners.TriggerListenerSupport;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobExecutionTrackingListener  extends TriggerListenerSupport implements JobListener {

    private final JobExecutionTrackingService trackingService;

    @Override
    public String getName() {
        return "JobExecutionTrackingListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        trackingService.recordJobToBeExecuted(context);
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        trackingService.recordJobExecutionVetoed(context);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (jobException != null) {
            trackingService.recordJobExecutionFailed(context, jobException);
        } else {
            trackingService.recordJobWasExecuted(context);
        }
    }
}
