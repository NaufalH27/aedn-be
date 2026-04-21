package com.aedn.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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
import com.aedn.dto.ProductPictureDto;
import com.aedn.dto.ReqProductDto;
import com.aedn.dto.ReqUploadProductPictureDto;
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
    
    @Value("${s3.endpoint.public:}")
    private String s3Endpoint;

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

    @PostMapping("/product/picture/signed-url/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductPictureDto>> getUploadUrl(@Valid @RequestBody ReqUploadProductPictureDto req) {
        String filename = UUID.randomUUID().toString() + "." + req.getImageExtension();
        ProductPictureDto productUrl = new ProductPictureDto();

        productUrl.setS3SignedUrl(s3Service.generateUploadUrl("public", "product/" + filename, req.getImageExtension()));

        if (this.s3Endpoint.isBlank()) {
            return ResponseEntity.ok(ApiResponse.failure("Signed Url Generation Failed", "S3_SIGNED_FAILED", "Unconfigurable object storage public endpoint"));
            

        }
        productUrl.setFilename(filename);
        productUrl.setUrl(s3Endpoint + "/product/" + filename);
        productUrl.setKey("product/" + filename);

        return ResponseEntity.ok(ApiResponse.success("Signed Url Generation Success", productUrl));
    }

}
