package com.aedn.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aedn.dto.ExampleDto;
import com.aedn.entity.Example;
import com.aedn.repository.ExampleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExampleService {
    
    private final ExampleRepository exampleRepository;

    public ExampleDto getExample() {
        Optional<Example> entity = exampleRepository.findFirstBy();
        return entity.map(ExampleDto::fromEntity).orElse(null);
    }

    public ExampleDto nothing(ExampleDto dto) {
        return dto;
    }


}
