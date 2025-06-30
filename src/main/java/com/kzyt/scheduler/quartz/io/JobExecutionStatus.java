package com.kzyt.scheduler.quartz.io;

public enum JobExecutionStatus {
    SCHEDULED,
    EXECUTING,
    COMPLETED,
    FAILED,
    VETOED,
    MISFIRED
}