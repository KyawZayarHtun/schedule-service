package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class QuartzSchedulerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8643932371566484994L;

    public QuartzSchedulerException(String message) {
        super(message);
    }

    public QuartzSchedulerException(String message, Throwable cause) {
        super(message, cause);
    }

}
