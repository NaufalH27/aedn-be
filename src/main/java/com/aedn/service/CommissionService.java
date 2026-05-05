package com.aedn.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aedn.dto.RequestCommissionDto;
import com.aedn.dto.RequestDto;
import com.aedn.entity.Product;
import com.aedn.entity.Request;
import com.aedn.entity.User;
import com.aedn.exception.ProductNotFoundException;
import com.aedn.repository.RequestRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommissionService {

    private final RequestRepository requestRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public RequestDto findById(UUID id) {
        return RequestDto.fromEntity(requestRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Order Not Found")));
    }

    public List<RequestDto> getAllRequest() {
        return requestRepository.findAll()
            .stream()
            .map(req -> RequestDto.fromEntity(req))
            .toList();
    }

    public RequestDto createRequest(RequestCommissionDto dto, UUID userId) {

        String prefix = "AEDNCOMMS";

        String date = java.time.LocalDate.now()
            .format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE); 

        Request request = new Request();
        request.setUser(entityManager.getReference(User.class, userId));
        request.setProduct(entityManager.getReference(Product.class, dto.getProductId()));

        request.setProposedDeadline(dto.getProposedDeadline());
        request.setProductTitle(dto.getProductTitle());
        request.setProposedPrice(dto.getPrice());
        request.setExtraInfo(dto.getExtraInfo());
        request.setUsername(dto.getUsername());
        request.setEmail(dto.getEmail());
        request.setCurrencyCode(dto.getCurrencyCode());
        request.setStatus("pending");
        request.setRequestNumberId(prefix + "#" + date + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase());
        return RequestDto.fromEntity(requestRepository.save(request));
    }
}
