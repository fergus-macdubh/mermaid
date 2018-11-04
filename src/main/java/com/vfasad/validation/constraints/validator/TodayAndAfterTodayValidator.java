package com.vfasad.validation.constraints.validator;

import com.vfasad.validation.constraints.TodayAndAfterToday;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class TodayAndAfterTodayValidator implements ConstraintValidator<TodayAndAfterToday, LocalDate> {
    public final void initialize(final TodayAndAfterToday annotation) {}

    public final boolean isValid(final LocalDate value,
                                 final ConstraintValidatorContext context) {
        LocalDate today = LocalDate.now();
        return value.compareTo(today) == 0 || value.compareTo(today) > 0;
    }
}
