package com.aedn.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostOrderDrawingProgressDto {
    private List<String> srcUrlKeys;
    private String name;
}
