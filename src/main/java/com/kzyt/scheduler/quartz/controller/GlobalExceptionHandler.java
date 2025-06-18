package com.kzyt.scheduler.quartz.controller;

import com.kzyt.scheduler.quartz.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(QuartzJobOrTriggerNotFoundException.class)
    public ResponseEntity<String> handleScheduleNotFound(QuartzJobOrTriggerNotFoundException e) {
        log.error("Job or Trigger not found: {}", e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(JobHasAssociatedTriggersException.class)
    public ResponseEntity<String> handleJobHasAssociatedTriggers(JobHasAssociatedTriggersException e) {
        log.error("Job has associated triggers: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(JobOrTriggerDeleteFailException.class)
    public ResponseEntity<String> handleJobDeleteFail(JobOrTriggerDeleteFailException e) {
        log.error("Job delete failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete job or trigger due to an internal server error.");
    }

    @ExceptionHandler(QuartzSchedulerException.class)
    public ResponseEntity<String> handleQuartzSchedulerException(QuartzSchedulerException e) {
        log.error("Quartz Scheduler Exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the request. Please try again later.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
//        e.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MissingParameterException.class)
    public ResponseEntity<String> handleMissingParameterException(MissingParameterException e) {
        log.error("Missing parameter: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }



}
