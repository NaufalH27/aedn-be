package com.aedn.mapper;

import org.springframework.stereotype.Component;

import com.aedn.dto.OrderDto;
import com.aedn.dto.UserDto;
import com.aedn.entity.Order;
import com.aedn.entity.Request;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ProductMapper productMapper;

    public OrderDto entityToDto(Order entity) {
        Request request = entity.getRequest();
        OrderDto dto = new OrderDto();

        dto.setId(entity.getId());
        dto.setRequestNumberId(request.getRequestNumberId());

        if (request.getProduct() != null) {
            dto.setProduct(productMapper.dtoFromEntity(request.getProduct()));
        }

        dto.setCurrencyCode(request.getCurrencyCode());
        dto.setProposedDeadline(request.getProposedDeadline());
        dto.setDeadline(entity.getDeadline());

        dto.setUsername(request.getUsername());
        dto.setEmail(request.getEmail());
        dto.setUser(UserDto.fromEntity(request.getUser()));  

        dto.setStatus(entity.getStatus());
        dto.setRequestStatus(request.getStatus());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setRequestCreatedAt(request.getCreatedAt());

        dto.setProposedPrice(request.getProposedPrice());
        dto.setPrice(entity.getPrice());

        dto.setExtraInfo(request.getExtraInfo());

        dto.setPaidStatus(entity.getPaidStatus());
        dto.setPaidAt(entity.getPaidAt());

        dto.setRating(entity.getRating());

        return dto;
    }
}
