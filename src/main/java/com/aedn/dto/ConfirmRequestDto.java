package com.aedn.dto;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmRequestDto {
    Instant deadline;
    Long price;
    List<String> sketchUrlKey;
}
