package com.bookshop.service.interf;

import com.bookshop.dto.UserRegistrationRequestDto;
import com.bookshop.dto.UserResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto user) throws RegistrationException;

    User get(Long id);
}
