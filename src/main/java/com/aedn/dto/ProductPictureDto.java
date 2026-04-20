package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductPictureDto {
    private String s3SignedUrl;
    private String filename;
    private String url;
}
