package com.aedn.mapper;

import org.springframework.stereotype.Component;

import com.aedn.dto.RequestDto;
import com.aedn.dto.RequestOrderSummaryDto;
import com.aedn.dto.UserDto;
import com.aedn.entity.Order;
import com.aedn.entity.Request;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RequestMapper {

    private final ProductMapper productMapper;

    public RequestDto dtoFromEntity(Request request) {
        RequestDto dto = new RequestDto();

        dto.setId(request.getId());
        dto.setRequestNumberId(request.getRequestNumberId());

        if (request.getProduct() != null) {
            dto.setProduct(productMapper.dtoFromEntity(request.getProduct()));
        }

        dto.setCurrencyCode(request.getCurrencyCode());
        dto.setProposedDeadline(request.getProposedDeadline());
        dto.setUsername(request.getUsername());
        dto.setEmail(request.getEmail());
        dto.setUser(UserDto.fromEntity(request.getUser()));  
        dto.setStatus(request.getStatus());
        dto.setExtraInfo(request.getExtraInfo());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setProposedPrice(request.getProposedPrice());
        if (request.getOrder() == null) {
            dto.setOrderSummary(null);
        } else {
            Order o = request.getOrder();
            dto.setOrderSummary(new RequestOrderSummaryDto(o.getId(), o.getPaidStatus(), o.getStatus()));
        }

        return dto;
    }

}
