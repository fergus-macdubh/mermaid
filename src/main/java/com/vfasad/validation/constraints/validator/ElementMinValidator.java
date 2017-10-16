package com.vfasad.validation.constraints.validator;

import com.vfasad.validation.constraints.ElementMin;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class ElementMinValidator implements ConstraintValidator<ElementMin, Collection> {
    private long value;

    @Override
    public void initialize(ElementMin elementMin) {
        value = elementMin.value();
    }

    @Override
    public boolean isValid(Collection collection, ConstraintValidatorContext constraintValidatorContext) {
        for (Object object : collection) {
            if (!(object instanceof Number)) return false;

            Number number = (Number) object;

            if (number.doubleValue() < value) {
                return false;
            }
        }
        return true;
    }
}
