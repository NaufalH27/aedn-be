package com.aedn.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestCommissionDto {
    private UUID productId;
    private String productTitle;

    private Instant proposedDeadline;

    private String extraInfo;

    private String username;

    private String email;

    private String currencyCode; // target/requested currency

    // optional: include if you want to return computed value
    private Long price;
}
