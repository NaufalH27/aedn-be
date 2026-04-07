package com.aedn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.dto.LoginDto;
import com.aedn.dto.LoginResponseDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.UserDto;
import com.aedn.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto form) {
        return ResponseEntity.ok(authService.login(form));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto form) {
        return ResponseEntity.ok(authService.createUser(form));
    }
}
