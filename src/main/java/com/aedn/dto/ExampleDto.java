package com.aedn.dto;


import java.util.Collections;
import java.util.List;

import com.aedn.entity.ExampleEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExampleDto {
    private String aString;
    private int aNumber;
    private float aFloat;
    private List<String> listOfStrings;

    public static ExampleDto fromEntity(ExampleEntity entity) {
        if (entity == null) return null;

        ExampleDto dto = new ExampleDto();
        dto.setAString(entity.getExampleString());
        dto.setANumber(entity.getExampleInteger());
        dto.setAFloat(entity.getExampleFloat());

        dto.setListOfStrings(Collections.nCopies(5, entity.getExampleString()));
        return dto;
    }
}
