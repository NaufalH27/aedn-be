package com.aedn.dto;

import com.aedn.entity.Category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
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
