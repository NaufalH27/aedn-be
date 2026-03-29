package com.aedn.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aedn.dto.ExampleDto;
import com.aedn.entity.Example;
import com.aedn.repository.ExampleRepository;

@Service
public class ExampleService {
    
    private final ExampleRepository exampleRepository;

    @Autowired
    public ExampleService(ExampleRepository exampleRepo) {
        this.exampleRepository = exampleRepo;
    }

    public ExampleDto getExample() {
        Optional<Example> entity = exampleRepository.findFirstBy();

        return entity.map(ExampleDto::fromEntity).orElse(null);
    }


}
