package com.aedn.service;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aedn.dto.LoginDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.TokenDto;
import com.aedn.dto.UserDto;
import com.aedn.entity.User;
import com.aedn.exception.UserCreationException;
import com.aedn.exception.UserLoginException;
import com.aedn.repository.UserRepository;
import com.aedn.security.JwtHelper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {
    
    @Autowired
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserDto createUser(SignUpDto form) {
        if (userRepository.existsByUsername(form.getUsername())) {
            throw new UserCreationException("Username already taken");
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            throw new UserCreationException("Email already registered");
        }
        
        String hashedPassword = bcryptHash(form.getPassword());
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(hashedPassword);
        user.setFullName(form.getFullName());

        User savedUser = userRepository.save(user);

        return UserDto.fromEntity(savedUser);
    }

    public TokenDto login(LoginDto form) {
        User user = new User();
        if (form.getLoginMethod().equals("email")) { 
            user = userRepository.findByEmail(form.getEmail()).orElseThrow();
        } else if (form.getLoginMethod().equals("username")) {
            user = userRepository.findByUsername(form.getUsername()).orElseThrow();
        } else {
            throw new UserLoginException("login method not found");
        }

        if (!compareBcrypt(form.getPassword(), user.getPassword())) {
            throw new UserLoginException("Wrong Password");
        }

        String refreshToken = generateRefreshToken(user.getId());
        String jwtToken = jwtHelper.generateToken(user.getId(), user.getUsername());
        TokenDto token = new TokenDto();
        token.setJwtToken(jwtToken);
        token.setRefreshToken(refreshToken);
        return token;
    }

    private String bcryptHash(String plain) {
        try {
            return encoder.encode(plain);
        } catch (Exception e) {
            throw new UserCreationException("Error encoding password: " + e.getMessage(), e);
        }
    }

    public TokenDto refreshToken(UUID userId) {
        String refreshToken = "";
        String jwtToken = "";
        TokenDto token = new TokenDto();
        token.setJwtToken(jwtToken);
        token.setRefreshToken(refreshToken);
        return token;
    }

    private boolean compareBcrypt(String plain, String hashed) {
        return encoder.matches(plain, hashed);
    }

    private String generateRefreshToken(UUID userId) {
        return "";
    }
}
