package com.aedn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import com.aedn.dto.OrderDrawingProgressDto;
import com.aedn.dto.OrderDto;
import com.aedn.dto.PostOrderDrawingProgressDto;
import com.aedn.entity.Order;
import com.aedn.entity.OrderDrawingProgress;
import com.aedn.exception.BadRequestException;
import com.aedn.exception.NotFoundException;
import com.aedn.infra.storage.S3PresignedUrlProvider;
import com.aedn.mapper.OrderMapper;
import com.aedn.repository.OrderDrawingProgressRepository;
import com.aedn.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderCommissionService {

    private final OrderRepository orderRepository;
    private final OrderDrawingProgressRepository orderDrawingProgressRepository;
    private final OrderMapper orderMapper;
    private final S3PresignedUrlProvider s3Provider;

    @PersistenceContext
    private EntityManager entityManager;

    String errStatusTransitionMsg = "Invalid Transition or Request Not Found, try reload the Page";

    @Transactional
    public void addNewDrawingProgress(UUID orderId, PostOrderDrawingProgressDto dto ) {
        List<OrderDrawingProgress> newDrawingProgress = new ArrayList<>();

        for (int i = 0; i < dto.getSrcUrlKeys().size(); i++) {
            OrderDrawingProgress drawing = new OrderDrawingProgress();
            drawing.setOrders(entityManager.getReference(Order.class, orderId));
            drawing.setPosition(i);
            drawing.setSrcUrlKey(dto.getSrcUrlKeys().get(i));
            drawing.setName(dto.getName());
            newDrawingProgress.add(drawing);
        }
        orderDrawingProgressRepository.saveAll(newDrawingProgress);
    }

    @Transactional
    public void finish(UUID orderId, List<String> srcUrlKeys) {
        int updated = orderRepository.markAsDone(orderId);
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }

        List<OrderDrawingProgress> newDrawingProgress = new ArrayList<>();

        for (int i = 0; i < srcUrlKeys.size(); i++) {
            OrderDrawingProgress drawing = new OrderDrawingProgress();
            drawing.setOrders(entityManager.getReference(Order.class, orderId));
            drawing.setPosition(i);
            drawing.setSrcUrlKey(srcUrlKeys.get(i));
            drawing.setName("finished");
            newDrawingProgress.add(drawing);
        }
        orderDrawingProgressRepository.saveAll(newDrawingProgress);
    }

    public List<OrderDrawingProgressDto> getDrawingProgress(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Not Found"));

        return order.getDrawingProgresses()
            .stream()
            .map(d -> toDtoDrawing(d))
        .toList();
    }

    public List<OrderDrawingProgressDto> getDrawingProgress(UUID orderId, UUID userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Not Found"));
        if (!(order.getRequest().getUser().getId().equals(userId))){
            throw new AuthorizationDeniedException("Not Authorized");
        }

        return order.getDrawingProgresses()
            .stream()
            .map(d -> toDtoDrawing(d))
        .toList();
    }

    public String getDrawingDownloadUrl(UUID drawingId, UUID userId) {
        OrderDrawingProgress drawing = orderDrawingProgressRepository.findById(drawingId)
            .orElseThrow(() -> new NotFoundException("Not Found"));
        if (!(drawing.getOrders().getRequest().getUser().getId().equals(userId))){
            throw new AuthorizationDeniedException("Not Authorized");
        }
        return generateDownloadDrawingUrl(drawing);
    }

    public String getDrawingDownloadUrl(UUID drawingId) {
        OrderDrawingProgress drawing = orderDrawingProgressRepository.findById(drawingId)
            .orElseThrow(() -> new NotFoundException("Not Found"));

        return generateDownloadDrawingUrl(drawing);
    }

    private String generateDownloadDrawingUrl(OrderDrawingProgress drawing) {
        String extension = "";
        String srcUrlKey = drawing.getSrcUrlKey();
        int lastDot = srcUrlKey.lastIndexOf('.');
        extension = srcUrlKey.substring(lastDot + 1);
        return s3Provider.generateDownloadUrl("private", srcUrlKey, drawing.getId() + extension);
    }

    private OrderDrawingProgressDto toDtoDrawing(OrderDrawingProgress drawing) {
        return OrderDrawingProgressDto
            .builder()
            .id(drawing.getId())
            .name(drawing.getName())
            .srcUrl(s3Provider.generateViewUrl("private", drawing.getSrcUrlKey()))
            .position(drawing.getPosition())
            .createdAt(drawing.getCreatedAt())
            .build();
    }

    @Transactional
    public void proceedWithoutPayment(UUID orderId) {
        int updated = orderRepository.proceedWithoutPayment(orderId);
        if (updated == 0) {
            throw new BadRequestException(errStatusTransitionMsg);
        }
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAllWithProductPictures()
            .stream()
            .map(o -> orderMapper.entityToDto(o))
            .toList();
    }

    public List<OrderDto> getUserOrders(UUID userId) {
        return orderRepository.findAllWithProductPicturesByUserId(userId)
            .stream()
            .map(o -> orderMapper.entityToDto(o))
            .toList();
    }
}
