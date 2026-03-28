package com.aedn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.dto.ExampleDto;
import com.aedn.service.ExampleService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class ExampleController {
    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleServ) {
        this.exampleService = exampleServ;
    }

    @GetMapping("/example")
    public ResponseEntity<ExampleDto> index() {
        return ResponseEntity.ok(exampleService.getExample());
    }
}
