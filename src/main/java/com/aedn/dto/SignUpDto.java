package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String redirectUrl;
}
