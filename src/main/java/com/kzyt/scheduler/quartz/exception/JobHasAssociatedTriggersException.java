package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class JobHasAssociatedTriggersException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2101679379225418613L;

    public JobHasAssociatedTriggersException(String message) {
        super(message);
    }
    
}
