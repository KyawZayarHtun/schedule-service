package com.kzyt.scheduler.quartz.config;

import com.kzyt.scheduler.quartz.listener.JobExecutionTrackingListener;
import com.kzyt.scheduler.quartz.listener.TriggerExecutionTrackingListener;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class QuartzTrackingConfiguration {

    @EventListener
    public void registerListeners(ContextRefreshedEvent event) throws SchedulerException {
        Scheduler scheduler = event.getApplicationContext().getBean(Scheduler.class);

        JobExecutionTrackingListener jobListener = event.getApplicationContext().getBean(JobExecutionTrackingListener.class);
        TriggerExecutionTrackingListener triggerListener = event.getApplicationContext().getBean(TriggerExecutionTrackingListener.class);

        scheduler.getListenerManager().addJobListener(jobListener);
        scheduler.getListenerManager().addTriggerListener(triggerListener);
    }

}
