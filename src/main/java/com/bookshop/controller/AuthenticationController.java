package com.bookshop.controller;

import com.bookshop.dto.UserLoginRequestDto;
import com.bookshop.dto.UserLoginResponseDto;
import com.bookshop.dto.UserRegistrationRequestDto;
import com.bookshop.dto.UserResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.security.AuthenticationService;
import com.bookshop.service.interf.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.login(request);
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
