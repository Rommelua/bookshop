package com.bookshop.dto;

import com.bookshop.dto.validation.PasswordMatch;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordMatch
public class UserRegistrationRequestDto {
    @Email
    private String email;
    private String password;
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
