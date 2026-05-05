package com.aedn.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.dto.RequestCommissionDto;
import com.aedn.dto.RequestDto;
import com.aedn.security.JwtUserPrincipal;
import com.aedn.common.ApiResponse;
import com.aedn.service.CommissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommissionController {

    private final CommissionService commissionService;

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RequestDto>>> getAllRequest() {
        return ResponseEntity.ok(ApiResponse.success("Get requests Success", commissionService.getAllRequest()));
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<ApiResponse<RequestDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Get request Success", commissionService.findById(id)));
    }

    @PostMapping("/requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<RequestDto>> createRequest(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody RequestCommissionDto dto
        ) {
        return ResponseEntity.ok(ApiResponse.success("Create Request Commission Success", commissionService.createRequest(dto, user.getId())));
    }
}
