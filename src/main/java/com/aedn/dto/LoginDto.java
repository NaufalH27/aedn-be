package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String username;
    private String email;
    private String password;
    private String loginMethod;
}
