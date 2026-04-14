package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class EditProductDto {
    long id;
    private String title;
    private Long price;
    private String currencyCode;
    private String description;
    private int quantity;
    private List<String> pictureUrls;
}
