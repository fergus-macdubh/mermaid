package com.vfasad.validation.constraints;

import com.vfasad.validation.constraints.validator.TodayAndAfterTodayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ FIELD, METHOD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TodayAndAfterTodayValidator.class)
@Documented
public @interface TodayAndAfterToday {

    String message() default "{javax.validation.constraints.TodayAndAfterToday.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
