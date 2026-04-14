package com.aedn.entity;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private long price;

    @Column(nullable=false)
    private String currencyCode;

    private String shortlink;

    @Column(nullable=false)
    private String title;

    private String description;
    
    @Column(nullable=false)
    private int quantity;

    private Instant createdAt = Instant.now();
    private Boolean isActive = true;
    private String urlSlug;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPicture> pictures;

}
