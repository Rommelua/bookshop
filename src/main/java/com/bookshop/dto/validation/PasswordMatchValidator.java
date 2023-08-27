package com.bookshop.dto.validation;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegistrationRequestDto> {
    public static final int MIN_PASSWORD_LENGTH = 4;

    @Override
    public boolean isValid(UserRegistrationRequestDto requestDto, ConstraintValidatorContext context) {
        String password = requestDto.getPassword();
        String repeatPassword = requestDto.getRepeatPassword();
        return password != null
                && password.length() >= MIN_PASSWORD_LENGTH
                && password.equals(repeatPassword);
    }
}
