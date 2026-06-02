package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class RequestDto {

    private UUID id;
    private String requestNumberId;
    private ProductDto product;
    private String currencyCode;
    private Instant proposedDeadline;
    private String username;
    private String email;
    private String status;
    private String extraInfo;
    private Instant createdAt;
    private Long proposedPrice;
    UserDto user;
    RequestOrderSummaryDto orderSummary;
}

