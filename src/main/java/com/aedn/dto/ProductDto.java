package com.aedn.dto;

import java.time.Instant;
import java.util.List;

import com.aedn.entity.Category;
import com.aedn.entity.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;
    private String title;
    private long price;
    private String urlSlug;
    private int quantity;
    private CategoryDto category;
    private String currencyCode;
    private Instant createdAt;
    private Boolean isActive;
    private List<String> pictureUrls;

    public static ProductDto fromEntity(Product entity) {
        return fromEntity(entity, null);
    }

    public static ProductDto fromEntity(Product entity, String s3Url) {
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

        if (s3Url == null || entity.getPictures() == null) {
            dto.setPictureUrls(List.of());
        } else {
            dto.setPictureUrls(
                    entity.getPictures()
                    .stream()
                    .map(p -> s3Url + "/" + p.getUrl())
                    .toList()
                    );
        }

        return dto;
    }
}

@Getter
@Setter
class CategoryDto {
    private String name;
    private Long id;

    public static CategoryDto fronEmtity(Category entity) {
        if (entity == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
