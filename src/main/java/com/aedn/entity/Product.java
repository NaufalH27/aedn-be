package com.aedn.entity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable=false)
    private long price;

    @Column(nullable=false)
    private String currencyCode;

    @Column(nullable=false)
    private String title;

    private String description;
    private String urlSlug;
    
    @Column(nullable=false)
    private int quantity;

    private Instant createdAt = Instant.now();
    private Instant deletedAt;

    private Boolean isActive = true;
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<ProductPicture> pictures;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
