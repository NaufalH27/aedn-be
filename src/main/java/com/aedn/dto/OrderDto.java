package com.aedn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
public class OrderDto {
    private UUID id;
    private UUID requestId;
    private String requestNumberId;
    private String requestStatus;
    private ProductDto product;
    private String currencyCode;
    private Instant proposedDeadline;
    private String username;
    private String email;
    private String extraInfo;
    private Instant requestCreatedAt;
    private Long proposedPrice;
    private UserDto user;

    private String status;
    private Instant deadline;
    private Long price;
    private String paidStatus;
    private Instant paidAt;
    private Integer rating;
    private Instant createdAt;
}
