package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {
    private String refreshToken;
    private String jwtToken;
}
