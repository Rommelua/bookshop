package com.bookshop.dto.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "Password is less then "
            + PasswordMatchValidator.MIN_PASSWORD_LENGTH + " symbols or "
            + "Password and RepeatPassword don't match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
