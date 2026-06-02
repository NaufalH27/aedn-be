package com.aedn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import com.aedn.dto.ConfirmRequestDto;
import com.aedn.dto.RequestCommissionDto;
import com.aedn.dto.RequestDto;
import com.aedn.entity.Order;
import com.aedn.entity.OrderDrawingProgress;
import com.aedn.entity.Product;
import com.aedn.entity.Request;
import com.aedn.entity.User;
import com.aedn.exception.BadRequestException;
import com.aedn.exception.NotFoundException;
import com.aedn.exception.ProductNotFoundException;
import com.aedn.mapper.RequestMapper;
import com.aedn.repository.OrderRepository;
import com.aedn.repository.RequestRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestCommissionService {

    private final RequestRepository requestRepository;
    private final OrderRepository orderRepository;
    private final RequestMapper requestMapper;

    String errStatusTransitionMsg = "Invalid Transition or Request Not Found, try reload the Page";

    @PersistenceContext
    private EntityManager entityManager;

    public RequestDto findByIdAndUserId(UUID id, UUID userId) {
        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Order Not Found"));
        if (!request.getUser().getId().equals(userId)) {
            throw new AuthorizationDeniedException("Unauthorized Access");
        }
        return requestMapper.dtoFromEntity(request);
    }

    public RequestDto findById(UUID id) {
        return requestMapper.dtoFromEntity(requestRepository.findByIdWithProductPictures(id)
            .orElseThrow(() -> new ProductNotFoundException("Order Not Found")));
    }

    public List<RequestDto> getAllRequests() {
        return requestRepository.findAllWithProductPictures()
            .stream()
            .map(req -> requestMapper.dtoFromEntity(req))
            .toList();
    }

    public List<RequestDto> getUserRequests(UUID userId) {
        return requestRepository.findWithProductPicturesByUserId(userId)
            .stream()
            .map(req -> requestMapper.dtoFromEntity(req))
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
        request.setProposedPrice(dto.getPrice());
        request.setExtraInfo(dto.getExtraInfo());
        request.setUsername(dto.getUsername());
        request.setEmail(dto.getEmail());
        request.setCurrencyCode(dto.getCurrencyCode());
        request.setStatus("pending");
        request.setRequestNumberId(prefix + "#" + date + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase());
        return requestMapper.dtoFromEntity(requestRepository.save(request));
    }

    @Transactional
    public void proceedRequest(UUID id) {
        int updated = requestRepository.changeStatus(id, List.of("pending"), "proceed");
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }
    }

    @Transactional
    public void rejectRequest(UUID id) {
        int updated = requestRepository.changeStatus(id, List.of("pending", "proceed"), "rejected");
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }
    }

    @Transactional
    public void confirmRequest(UUID id, ConfirmRequestDto dto) {
        int updated = requestRepository.changeStatus(id, List.of("proceed"), "confirmed");
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }

        if (orderRepository.existsById(id)) {
            throw new BadRequestException("Order already exists for this request");
        }

        Request request = requestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Request not found"));

        Order order = new Order();
        order.setRequest(request);
        order.setDeadline(dto.getDeadline());
        order.setPrice(dto.getPrice());
        order.setStatus("pending_payment");
        order.setPaidStatus("unpaid");
        order.setDrawingProgresses(createOrderSketches(dto.getSketchUrlKey(), order));
        orderRepository.save(order);
    }

    @Transactional
    public void cancelRequest(UUID id, UUID userId) {
        int updated = requestRepository.changeStatusByUserId(id, userId, List.of("pending", "proceed"), "cancelled");
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }
    }

    private List<OrderDrawingProgress> createOrderSketches(List<String> pictureUrls, Order order) {
        List<OrderDrawingProgress> sketches = new ArrayList<>();
        if (pictureUrls == null) {
            return sketches;
        }

        for (int i = 0; i < pictureUrls.size(); i++) {
            OrderDrawingProgress sketch = new OrderDrawingProgress();
            sketch.setOrders(order);
            sketch.setPosition(i);
            sketch.setSrcUrlKey(pictureUrls.get(i));
            sketch.setName("sketch");
            sketches.add(sketch);
        }

        return sketches;
    }
}
