package com.aedn.dto;

import com.aedn.entity.Request;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class RequestDto {

    private UUID id;
    private String requestNumberId;
    private UUID productId;
    private String currencyCode;
    private Instant proposedDeadline;
    private String username;
    private String email;
    private String status;
    private String extraInfo;
    private Instant createdAt;
    private String productTitle;
    private Long proposedPrice;

    public static RequestDto fromEntity(Request request) {
        if (request == null) {
            return null;
        }

        RequestDto dto = new RequestDto();

        dto.setId(request.getId());
        dto.setRequestNumberId(request.getRequestNumberId());

        if (request.getProduct() != null) {
            dto.setProductId(request.getProduct().getId());
        }

        dto.setCurrencyCode(request.getCurrencyCode());
        dto.setProposedDeadline(request.getProposedDeadline());
        dto.setUsername(request.getUsername());
        dto.setEmail(request.getEmail());
        dto.setStatus(request.getStatus());
        dto.setExtraInfo(request.getExtraInfo());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setProductTitle(request.getProductTitle());
        dto.setProposedPrice(request.getProposedPrice());

        return dto;
    }
}
