package com.bookshop.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String email;
    private String password;
}
