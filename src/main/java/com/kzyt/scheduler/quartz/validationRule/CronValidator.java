package com.kzyt.scheduler.quartz.validationRule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.quartz.CronExpression;

public class CronValidator implements ConstraintValidator<ValidCron, String> {

    @Override
    public boolean isValid(String cron, ConstraintValidatorContext constraintValidatorContext) {

        if (cron == null || cron.isBlank()) {
            return false;
        }

        return CronExpression.isValidExpression(cron);
    }
}
