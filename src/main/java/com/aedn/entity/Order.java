package com.aedn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Request request;

    private String status = "pending_payment";

    @Column(nullable = false)
    private Instant deadline;

    @Column(nullable = false)
    private Long price;

    private String paidStatus = "unpaid";

    private Instant paidAt;

    @Column(nullable = true)
    private Integer rating;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("created_at ASC")
    private List<OrderDrawingProgress> drawingProgresses;
}
