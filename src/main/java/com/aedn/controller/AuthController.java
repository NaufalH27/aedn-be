package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.dto.LoginDto;
import com.aedn.dto.RefreshTokenDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.TokenDto;
import com.aedn.dto.UserDto;
import com.aedn.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenDto>> login(@RequestBody LoginDto form) {
        return ResponseEntity.ok(ApiResponse.success("Login Success", authService.login(form)));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@RequestBody SignUpDto form) {
        return ResponseEntity.ok(ApiResponse.success("Sign Up Success", authService.createUser(form)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenDto>> refreshToken(@RequestBody RefreshTokenDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Refresh Token Success", authService.refreshToken(dto)));
    }
}
