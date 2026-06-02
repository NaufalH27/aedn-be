package com.aedn.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aedn.dto.UserDto;
import com.aedn.exception.NotFoundException;
import com.aedn.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;

    public UserDto getById(UUID id) {
        return UserDto.fromEntity(
                userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cant get user information, might be invalid session, please login again"))
            );
    }
}
