package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class JobDeleteFailException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2328135938536160892L;

    public JobDeleteFailException(String message) {
        super(message);
    }

    public JobDeleteFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
