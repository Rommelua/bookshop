package com.bookshop.service.interf;

import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto user) throws RegistrationException;

    User get(Long id);
}
