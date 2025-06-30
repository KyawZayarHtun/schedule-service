package com.kzyt.scheduler.quartz.entity;

import com.kzyt.scheduler.quartz.io.JobExecutionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "job_execution_history")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionHistory  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Column(name = "job_group", nullable = false)
    private String jobGroup;

    @Column(name = "trigger_name", nullable = false)
    private String triggerName;

    @Column(name = "trigger_group", nullable = false)
    private String triggerGroup;

    @Column(name = "scheduled_fire_time")
    private Date scheduledFireTime;

    @Column(name = "actual_fire_time")
    private Date actualFireTime;

    @Column(name = "job_start_time")
    private Date jobStartTime;

    @Column(name = "job_end_time")
    private Date jobEndTime;

    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_status")
    private JobExecutionStatus executionStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "is_misfire")
    private Boolean isMisfire = false;

    @Column(name = "next_fire_time")
    private Date nextFireTime;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    public JobExecutionHistory(String jobName, String jobGroup, String triggerName, String triggerGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
    }

}
