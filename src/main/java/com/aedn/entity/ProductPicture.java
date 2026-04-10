package com.aedn.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"product_id", "position"})
    }
)
public class ProductPicture {

    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable=false)
    private String url;

    @Column(nullable=false)
    private int position;

    private Instant createdAt = Instant.now();
}
