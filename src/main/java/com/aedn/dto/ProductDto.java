package com.aedn.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private UUID id;
    private String title;
    private long price;
    private String urlSlug;
    private int quantity;
    private CategoryDto category;
    private String currencyCode;
    private String description;
    private Instant createdAt;
    private Boolean isActive;
    private Boolean isDeleted;
    private List<String> pictureUrls;
}

