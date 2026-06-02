package com.aedn.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.aedn.exception.InvalidRefreshTokenException;
import com.aedn.exception.UserCreationException;
import com.aedn.exception.UserLoginException;
import com.aedn.repository.RefreshTokenRepository;
import com.aedn.repository.UserRepository;
import com.aedn.security.JwtHelper;

import jakarta.transaction.Transactional;
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
    private static final Base64.Decoder base64Decoder = Base64.getUrlDecoder();


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

    public TokenDto login(LoginDto form) throws NoSuchAlgorithmException {
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
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String base64token = base64Encoder.encodeToString(randomBytes);

        List<String> roles = new ArrayList<>(List.of("ROLE_USER"));
        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            roles.add("ROLE_ADMIN");
        }
        
        String jwtToken = jwtHelper.generateToken(user.getId(), user.getUsername(), user.getEmail(), roles);
        Instant expiration = Instant.now().plus(jwtConfig.getRefreshTokenExpirationTime());
        RefreshToken refreshToken = refreshTokenRepository.save(new RefreshToken(bytesToSha256HexString(randomBytes), user.getId(), expiration));
        return new TokenDto(jwtToken, RefreshTokenDto.fromEntity(refreshToken, base64token));
    }

    @Transactional
    public TokenDto refreshToken(String token) throws NoSuchAlgorithmException {
        byte[] byteToken = base64Decoder.decode(token); 
        RefreshToken refreshToken = refreshTokenRepository.findByToken(bytesToSha256HexString(byteToken))
            .orElseThrow(() -> new InvalidRefreshTokenException("Invalid Session, Please Login Again"));

        if (Instant.now().isAfter(refreshToken.getExpiresAt())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("Session Expired, Please Login Again");
        }

        User user = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new InvalidRefreshTokenException("Invalid Session, Please Login Again"));

        List<String> roles = new ArrayList<>(List.of("ROLE_USER"));
        if (Boolean.TRUE.equals(user.getIsAdmin())) {
            roles.add("ROLE_ADMIN");
        }
        
        String jwtToken = jwtHelper.generateToken(user.getId(), user.getUsername(), user.getEmail(), roles);
        Instant expiration = Instant.now().plus(jwtConfig.getRefreshTokenExpirationTime());
        refreshToken.setExpiresAt(expiration);
        refreshTokenRepository.save(refreshToken);
        return new TokenDto(jwtToken, RefreshTokenDto.fromEntity(refreshToken, token));
    }

    private String bytesToSha256HexString(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashByte = digest.digest(bytes);

        StringBuilder sb = new StringBuilder();

        for (byte b: hashByte) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
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
