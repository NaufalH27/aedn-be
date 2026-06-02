package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.dto.UserDto;
import com.aedn.security.JwtUserPrincipal;
import com.aedn.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<UserDto>> getMyInfo(@AuthenticationPrincipal JwtUserPrincipal user) {
        return ResponseEntity.ok(ApiResponse.success("Proceed Request Commission Success", userService.getById(user.getId()) ));
    }

}
