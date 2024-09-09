package com.example.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            String firstFieldValue = BeanUtils.getProperty(value, firstField);
            String secondFieldValue = BeanUtils.getProperty(value, secondField);

            return firstFieldValue != null && firstFieldValue.equals(secondFieldValue);
        } catch (Exception e) {
            return false;
        }
    }
}
