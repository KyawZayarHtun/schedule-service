package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class JobOrTriggerDeleteFailException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2328135938536160892L;

    public JobOrTriggerDeleteFailException(String message) {
        super(message);
    }

    public JobOrTriggerDeleteFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
