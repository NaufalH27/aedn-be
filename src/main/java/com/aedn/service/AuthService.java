package com.aedn.service;


import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aedn.config.JwtConfig;
import com.aedn.dto.LoginDto;
import com.aedn.dto.RefreshTokenDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.TokenDto;
import com.aedn.dto.UserDto;
import com.aedn.entity.RefreshToken;
import com.aedn.entity.User;
import com.aedn.exception.UserCreationException;
import com.aedn.exception.UserLoginException;
import com.aedn.exception.UserRefreshTokenException;
import com.aedn.repository.RefreshTokenRepository;
import com.aedn.repository.UserRepository;
import com.aedn.security.JwtHelper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHelper jwtHelper;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder encoder;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();


    // TODO : Create email verification flow
    public UserDto createUser(SignUpDto form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new UserCreationException("Username already taken");
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            throw new UserCreationException("Email already registered");
        }
        
        String hashedPassword = encoder.encode(form.getPassword());
        User user = new User(form.getUsername(), form.getEmail(), hashedPassword);
        user.setFullName(form.getFullName());

        User savedUser = userRepository.save(user);
        return UserDto.fromEntity(savedUser);
    }

    public TokenDto login(LoginDto form) {
        User user;
        if (form.getLoginMethod().equals("email")) { 
            user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(() -> new UserLoginException("Invalid Credentials"));
        } else if (form.getLoginMethod().equals("username")) {
            user = userRepository.findByUsername(form.getUsername())
                .orElseThrow(() -> new UserLoginException("Invalid Credentials"));
        } else {
            throw new UserLoginException("login method not found");
        }

        if (!compareBcrypt(form.getPassword(), user.getPassword())) {
            throw new UserLoginException("Invalid Credentials");
        }

        return generateToken(user);
    }

    public TokenDto refreshToken(RefreshTokenDto refreshToken) {
        RefreshToken currToken = refreshTokenRepository.findByToken(refreshToken.getToken())
            .orElseThrow(() -> new UserRefreshTokenException("Invalid Session"));
        if (Instant.now().isAfter(currToken.getExpiresAt())){
            refreshTokenRepository.delete(currToken);
            throw new UserRefreshTokenException("Session Expired");
        }
        User user = userRepository.findById(currToken.getUserId())
            .orElseThrow(() -> new UserRefreshTokenException("Invalid Session"));
        refreshTokenRepository.delete(currToken);
        return generateToken(user);
    
    }

    private TokenDto generateToken(User user) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String base64token = base64Encoder.encodeToString(randomBytes);

        List<String> roles = new ArrayList<>(List.of("ROLE_USER"));
        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            roles.add("ROLE_ADMIN");
        }
        
        String jwtToken = jwtHelper.generateToken(user.getId(), user.getUsername(), user.getEmail(), roles);
        Instant expiration = Instant.now().plus(jwtConfig.getRefreshTokenExpirationTime());
        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(base64token, user.getId(), expiration));
        return new TokenDto(jwtToken, refreshToken.getToken());
    }

    private boolean compareBcrypt(String plain, String hashed) {
        return encoder.matches(plain, hashed);
    }

    public void createAdmin(String username, String email, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new UserCreationException("Username already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserCreationException("Email already registered");
        }
        
        String hashedPassword = encoder.encode(password);
        User user = new User(username, email, hashedPassword);
        user.setIsAdmin(true);

        userRepository.save(user);
    }

}
