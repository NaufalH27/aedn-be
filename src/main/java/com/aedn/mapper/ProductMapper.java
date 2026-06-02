package com.aedn.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aedn.dto.CategoryDto;
import com.aedn.dto.ProductDto;
import com.aedn.entity.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    @Value("${s3.endpoint.public:}")
    private String s3Endpoint;

    public ProductDto dtoFromEntity(Product entity) {
        ProductDto dto = new ProductDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setQuantity(entity.getQuantity());
        dto.setUrlSlug(entity.getUrlSlug());
        dto.setCategory(CategoryDto.fronEmtity(entity.getCategory()));
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDescription(entity.getDescription());
        dto.setIsDeleted(entity.getIsDeleted());

        if (s3Endpoint == null || s3Endpoint.isBlank() || entity.getPictures() == null) {
            dto.setPictureUrls(List.of());
        } else {
            dto.setPictureUrls(
                    entity.getPictures()
                    .stream()
                    .map(p -> s3Endpoint + "/" + p.getUrl())
                    .toList()
                );
        }

        return dto;
    }
}
