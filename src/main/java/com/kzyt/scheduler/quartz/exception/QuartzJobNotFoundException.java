package com.kzyt.scheduler.quartz.exception;

import java.io.Serial;

public class QuartzJobNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5945264028565746326L;

    public QuartzJobNotFoundException(String message) {
        super(message);
    }
}
