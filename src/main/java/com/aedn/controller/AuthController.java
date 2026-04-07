package com.aedn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.dto.ExampleDto;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "${apiPrefix}/users")
public class AuthController {


    @PostMapping("/createToken")
    public ResponseEntity<Object> createToken() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/signup")
    public ResponseEntity<ExampleDto> signup() {
        return ResponseEntity.ok(null);
    }
}
