package com.aedn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "request_number_id", nullable = false, unique = true)
    private String requestNumberId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "currency_code", length = 5)
    private String currencyCode;

    @Column(name = "proposed_deadline", nullable = false)
    private Instant proposedDeadline;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    private String status; // pending, proposed, rejected

    @Column(name = "extra_info")
    private String extraInfo;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "proposed_price")
    private Long proposedPrice;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
