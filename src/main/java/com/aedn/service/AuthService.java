package com.aedn.service;


import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties.Apiversion.Use;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aedn.dto.LoginDto;
import com.aedn.dto.LoginResponseDto;
import com.aedn.dto.SignUpDto;
import com.aedn.dto.UserDto;
import com.aedn.entity.User;
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
        String hashedPassword = bcryptHash(form.getPassword());
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setPassword(hashedPassword);
        user.setFullName(form.getFullName());

        User savedUser = userRepository.save(user);

        return UserDto.fromEntity(savedUser);
    }

    public LoginResponseDto login(LoginDto form) {
        User user = new User();
        if (form.getLoginMethod().equals("email")) { 
            user = userRepository.findByEmail(form.getEmail()).orElseThrow();
        } else if (form.getLoginMethod().equals("username")) {
            user = userRepository.findByUsername(form.getUsername()).orElseThrow();
        } else {
            throw new RuntimeException();
        }

        if (!compareBcrypt(form.getPassword(), user.getPassword())) {
            throw new RuntimeException();
        }

        UserDto userDto = UserDto.fromEntity(user);
        String refreshToken = refreshToken(user.getId());
        String jwtToken = jwtHelper.generateToken(user.getId(), user.getUsername());
        LoginResponseDto res = new LoginResponseDto();
        res.setUser(userDto);
        res.setJwtToken(jwtToken);
        res.setRefreshToken(refreshToken);
        return res;

    }

    private String bcryptHash(String plain) {
        try {
            return encoder.encode(plain);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding password: " + e.getMessage(), e);
        }
    }

    private boolean compareBcrypt(String plain, String hashed) {
        return encoder.matches(plain, hashed);
    }

    private String refreshToken(UUID userId) {
        return "";

    }

}
