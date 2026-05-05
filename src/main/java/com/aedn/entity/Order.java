package com.aedn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "request_id", columnDefinition = "uuid")
    private UUID requestId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "request_id")
    private Request request;

    private Integer paymentMethod;

    private String email;

    // pending, cancelled, processing, done
    private String status;

    @Column(nullable = false)
    private Instant deadline;

    private Boolean isPaid;

    private Instant paidAt;

    @Column(nullable = true)
    private Integer rating;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();
}
