package com.aedn.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.dto.ConfirmRequestDto;
import com.aedn.dto.RequestCommissionDto;
import com.aedn.dto.RequestDto;
import com.aedn.security.JwtUserPrincipal;
import com.aedn.service.RequestCommissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RequestCommissionController {

    private final RequestCommissionService commissionService;

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RequestDto>>> getAllRequest() {
        return ResponseEntity.ok(ApiResponse.success("Get requests Success", commissionService.getAllRequests()));
    }

    @GetMapping("/requests/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<RequestDto>>> getMyRequests(
            @AuthenticationPrincipal JwtUserPrincipal user
        ) {
        return ResponseEntity.ok(ApiResponse.success("Get requests Success", commissionService.getUserRequests(user.getId())));
    }

    @GetMapping("/requests/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RequestDto>> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal JwtUserPrincipal user,
            Authentication authentication
        ) {

        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        RequestDto dto;

        if (isAdmin) {
            dto = commissionService.findById(id);
        } else {
            dto = commissionService.findByIdAndUserId(id, user.getId());
        }

        return ResponseEntity.ok(ApiResponse.success("Get request Success", dto));
    }

    @PostMapping("/requests")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<RequestDto>> createRequest(
            @AuthenticationPrincipal JwtUserPrincipal user,
            @RequestBody RequestCommissionDto dto
        ) {
        return ResponseEntity.ok(ApiResponse.success("Create Request Commission Success", commissionService.createRequest(dto, user.getId())));
    }

    @PutMapping("/requests/{id}/proceed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> proceedRequest(@PathVariable UUID id) {
        commissionService.proceedRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Proceed Request Commission Success", null));
    }

    @PutMapping("/requests/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(@PathVariable UUID id) {
        commissionService.rejectRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Proceed Request Commission Success", null));
    }

    @PutMapping("/requests/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Void>> cancelRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal JwtUserPrincipal user
        ) {
        commissionService.cancelRequest(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Proceed Request Commission Success", null));
    }

    @PostMapping("/requests/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> confirmRequest(
            @PathVariable UUID id,
            @RequestBody ConfirmRequestDto dto
        ) {
        commissionService.confirmRequest(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Proceed Request Commission Success", null));
    }
}
