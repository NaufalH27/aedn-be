package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ReqProductDto {
    private String title;
    private long price;
    private String currencyCode;
    private String description;
    private int quantity;
    private String categoryName;
    private List<String> pictureUrls;
    private Boolean isActive;
}
