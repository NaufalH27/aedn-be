package com.aedn.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.config.JwtConfig;
import com.aedn.dto.LoginDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.TokenDto;
import com.aedn.dto.TokenJwtDto;
import com.aedn.dto.UserDto;
import com.aedn.exception.MissingRefreshTokenException;
import com.aedn.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenJwtDto>> login(@RequestBody LoginDto form) throws NoSuchAlgorithmException {
        TokenDto token = authService.login(form);
        ResponseCookie refreshCookie = createRefreshTokenCookie(token.getRefreshToken().getRawToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success(
                        "Login Success",
                        new TokenJwtDto(token.getAccessToken())
                ));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> signup(@RequestBody SignUpDto form) {
        return ResponseEntity.ok(ApiResponse.success("Sign Up Success", authService.createUser(form)));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<TokenJwtDto>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) throws NoSuchAlgorithmException {
        if (refreshToken == null) {
            throw new MissingRefreshTokenException("Unauthenticated, Please Login");
        }

        TokenDto token = authService.refreshToken(refreshToken);

        ResponseCookie refreshCookie = createRefreshTokenCookie(
                token.getRefreshToken().getRawToken()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiResponse.success(
                        "Refresh Token Success",
                        new TokenJwtDto(token.getAccessToken())
                ));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                // .secure(true) //TODO: Uncomment This in production
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success("Logout Success", null));
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                // .secure(true) //TODO: Uncomment This in production
                .sameSite("Lax")
                .path("/")
                .maxAge(jwtConfig.getRefreshTokenExpirationTime())
                .build();
    }
}
