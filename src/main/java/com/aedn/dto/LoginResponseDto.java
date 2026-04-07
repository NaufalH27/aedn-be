package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private UserDto user;
    private String refreshToken;
    private String jwtToken;
}
