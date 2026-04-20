package com.aedn.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUploadProductPictureDto {
    @Pattern(
    regexp = "^(?i)(png|jpg|jpeg|gif|webp|bmp|tiff|avif)$",
    message = "Invalid image extension, currently supported extension are: png, jpg, jpeg, gif, webp, bmp, tiff, avif"
    )
    @NotBlank(message = "Image type is required")
    private String imageExtension;
}

