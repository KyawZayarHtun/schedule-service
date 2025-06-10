package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class QuartzJobOrTriggerNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5945264028565746326L;

    public QuartzJobOrTriggerNotFoundException(String message) {
        super(message);
    }
}
