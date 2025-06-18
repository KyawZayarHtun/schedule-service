package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class MissingParameterException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -5009777061190207941L;

    public MissingParameterException(String message) {
        super(message);
    }
}
