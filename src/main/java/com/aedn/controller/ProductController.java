package com.aedn.controller;

import java.util.List;

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
import com.aedn.security.JwtUserPrincipal;
import com.aedn.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

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
        @PathVariable Long id,
        @RequestBody ReqProductDto dto
      ) {

        return ResponseEntity.ok(
            ApiResponse.success(
              "Edit Product Success",
              productService.editProduct(user.getId(), id, dto)
              )
            );
        }

    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok(ApiResponse.success("Delete Product Success", null));
    }
}
