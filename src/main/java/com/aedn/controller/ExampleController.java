package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.dto.ExampleDto;
import com.aedn.common.ApiResponse;
import com.aedn.service.ExampleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @GetMapping("/example")
    public ResponseEntity<ApiResponse<ExampleDto>> getExample() {
        return ResponseEntity.ok(ApiResponse.success("Get Example Success", exampleService.getExample()));
    }

    @PostMapping("/example")
    public ResponseEntity<ApiResponse<ExampleDto>> postExample(@RequestBody ExampleDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Post Example Success", exampleService.nothing(dto)));
    }
}
