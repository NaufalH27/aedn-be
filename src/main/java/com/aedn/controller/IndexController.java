package com.aedn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
public class IndexController {
    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> me() {
        return ResponseEntity.ok("amogus");
    }
}
