package com.aedn.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderDrawingProgressDto {
    private UUID id;
    private String srcUrl;
    private String name;
    private Integer position;
    private Instant createdAt;
}
