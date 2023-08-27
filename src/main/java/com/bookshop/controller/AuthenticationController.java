package com.bookshop.controller;

import com.bookshop.dto.user.UserLoginRequestDto;
import com.bookshop.dto.user.UserLoginResponseDto;
import com.bookshop.dto.user.UserRegistrationRequestDto;
import com.bookshop.dto.user.UserResponseDto;
import com.bookshop.exception.RegistrationException;
import com.bookshop.security.AuthenticationService;
import com.bookshop.service.interf.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
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
