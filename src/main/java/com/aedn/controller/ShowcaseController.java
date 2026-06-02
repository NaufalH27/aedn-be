package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ShowcaseController {

    // @GetMapping("/showcase")
    // public ResponseEntity<ApiResponse<>> getShowcase() {
    //     return ResponseEntity.ok(ApiResponse.success("Get showcase success", );
    // }
    //
    // @PostMapping("/showcase")
    // public ResponseEntity<ApiResponse<>> postShowcase(@RequestBody ExampleDto dto) {
    //     return ResponseEntity.ok(ApiResponse.success("Post Showcase Success", );
    // }
}
