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
import com.aedn.dto.OrderDrawingProgressDto;
import com.aedn.dto.OrderDto;
import com.aedn.dto.PostOrderDrawingProgressDto;
import com.aedn.dto.S3UploadPictureDto;
import com.aedn.dto.S3UploadPictureRequestDto;
import com.aedn.infra.storage.S3PresignedUrlProvider;
import com.aedn.mapper.S3PictureMapper;
import com.aedn.security.JwtUserPrincipal;
import com.aedn.service.OrderCommissionService;
import com.aedn.service.S3Service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderCommissionController {

    private final OrderCommissionService orderCommissionService;
    private final S3Service s3Service;

    @GetMapping("/orders/drawings/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDrawingProgressDto>>> getViewUrlAdmin(
            @PathVariable UUID id,
            @AuthenticationPrincipal JwtUserPrincipal user,
            Authentication authentication
        ) {

        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        List<OrderDrawingProgressDto> drawingProgress = isAdmin ? 
            orderCommissionService.getDrawingProgress(id) : orderCommissionService.getDrawingProgress(id, user.getId());

        return ResponseEntity.ok(ApiResponse.success("Signed Url Generation Success", drawingProgress));
    }

    @PostMapping("/orders/{id}/drawings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addNewDrawings(@PathVariable UUID id, @RequestBody PostOrderDrawingProgressDto dto) {
        orderCommissionService.addNewDrawingProgress(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Get orders Success", null ));
    }

    @GetMapping("/orders/drawings/{id}/download-url")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> get(
            @PathVariable UUID id,
            @AuthenticationPrincipal JwtUserPrincipal user,
            Authentication authentication
            ) {
        boolean isAdmin = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role -> role.equals("ROLE_ADMIN"));

        String url = isAdmin ?
            orderCommissionService.getDrawingDownloadUrl(id) : orderCommissionService.getDrawingDownloadUrl(id, user.getId());

        return ResponseEntity.ok(ApiResponse.success("Get orders Success", url));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success("Get orders Success", orderCommissionService.getAllOrders()));
    }

    @PutMapping("/orders/{id}/proceed-without-payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> proceedOrderWithoutPayment(@PathVariable UUID id) {
        orderCommissionService.proceedWithoutPayment(id);
        return ResponseEntity.ok(ApiResponse.success("Get orders Success", null));
    }

    @PutMapping("/orders/{id}/finish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> finishOrder(@PathVariable UUID id, @RequestBody PostOrderDrawingProgressDto dto) {
        orderCommissionService.finish(id, dto.getSrcUrlKeys());
        return ResponseEntity.ok(ApiResponse.success("Get orders Success", null));
    }

    @GetMapping("/orders/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(
            @AuthenticationPrincipal JwtUserPrincipal user
    ) {
        return ResponseEntity.ok(ApiResponse.success("Get orders Success", orderCommissionService.getUserOrders(user.getId())));
    }

    @PostMapping("/orders/drawings/signed-url")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<S3UploadPictureDto>> getUploadUrl(@Valid @RequestBody S3UploadPictureRequestDto req) {
        return ResponseEntity.ok(ApiResponse.success("Signed Url Generation Success", s3Service.generateUploadPrivateBucketUrl(req, "private", "drawings")));
    }
}
