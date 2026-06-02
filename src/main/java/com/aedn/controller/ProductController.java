package com.aedn.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aedn.common.ApiResponse;
import com.aedn.dto.ProductDto;
import com.aedn.dto.ReqProductDto;
import com.aedn.dto.S3UploadPictureDto;
import com.aedn.dto.S3UploadPictureRequestDto;
import com.aedn.infra.storage.S3PresignedUrlProvider;
import com.aedn.mapper.S3PictureMapper;
import com.aedn.security.JwtUserPrincipal;
import com.aedn.service.ProductService;
import com.aedn.service.S3Service;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
        @AuthenticationPrincipal JwtUserPrincipal user,
        @RequestBody ReqProductDto dto
        ) {
        return ResponseEntity.ok(ApiResponse.success("Create Product Success", productService.createProduct(user.getId(), dto)));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        return ResponseEntity.ok(ApiResponse.success("Get Product Success", productService.getAllProducts()));
    }

    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDto>> editProduct(
        @AuthenticationPrincipal JwtUserPrincipal user,
        @PathVariable UUID id,
        @RequestBody ReqProductDto dto
      ) {

        return ResponseEntity.ok(
            ApiResponse.success(
              "Edit Product Success",
              productService.editProduct(user.getId(), id, dto)
              )
            );
        }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Get Product Success", productService.getById(id)));
    }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(ApiResponse.success("Delete Product Success", null));
    }


    @PostMapping("/product/picture/signed-url/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<S3UploadPictureDto>> getUploadUrl(@Valid @RequestBody S3UploadPictureRequestDto req) {
        return ResponseEntity.ok(ApiResponse.success("Signed Url Generation Success", s3Service.generateUploadPublicBucketUrl(req, "product")));
    }
}
