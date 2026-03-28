package com.aedn.service;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aedn.dto.ExampleDto;
import com.aedn.entity.ExampleEntity;
import com.aedn.repository.ExampleRepository;

@Service
public class ExampleService {
    
    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleService(ExampleRepository exampleRepo) {
        this.exampleRepository = exampleRepo;
    }

    public ExampleDto getExample() {
        ExampleEntity something = exampleRepository.example();

        return ExampleDto.fromEntity(something);
    }


}
