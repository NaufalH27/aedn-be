package com.aedn.dto;


import java.util.UUID;

import com.aedn.entity.Portofolio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortofolioDto {
    private UUID id;
    private String pictureUrl;
    private String type;
    private int position;

    public static PortofolioDto fromEntity(Portofolio entity) {
        if (entity == null) return null;

        PortofolioDto dto = new PortofolioDto();
        dto.setId(entity.getId());
        dto.setPictureUrl(entity.getPictureUrl());
        dto.setType(entity.getType());
        dto.setPosition(entity.getPosition());
        return dto;
    }
}
