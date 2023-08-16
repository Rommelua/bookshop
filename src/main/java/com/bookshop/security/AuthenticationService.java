package com.bookshop.security;

import com.bookshop.dto.UserLoginRequestDto;
import com.bookshop.dto.UserLoginResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto login(UserLoginRequestDto request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        List<String> roles = authentication.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
        String token = jwtUtil.generateToken(request.getEmail(), roles);
        return new UserLoginResponseDto(token);
    }
}
