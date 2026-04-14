package com.aedn.dto;

import java.util.List;

import com.aedn.entity.Product;
import com.aedn.entity.ProductPicture;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private Long id;
    private String title;
    private long price;
    private String urlSlug;
    private String shortlink;
    private int quantity;
    private String currencyCode;
    private List<String> pictureUrls;

    public static ProductDto fromEntity(Product entity) {
        ProductDto dto = new ProductDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setQuantity(entity.getQuantity());
        dto.setUrlSlug(entity.getUrlSlug());
        dto.setShortlink(entity.getShortlink());

        dto.setPictureUrls(
            entity.getPictures() == null ? List.of() :
                entity.getPictures()
                    .stream()
                    .map(ProductPicture::getUrl)
                    .toList()
            );

        return dto;
    }
}
