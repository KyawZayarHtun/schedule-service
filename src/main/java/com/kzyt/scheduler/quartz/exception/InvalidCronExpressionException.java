package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class InvalidCronExpressionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2847256502203224311L;

    public InvalidCronExpressionException(String message) {
        super(message);
    }

}
