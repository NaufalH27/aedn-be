package com.aedn.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Portofolio {
    
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable=false)
    private String pictureUrl;

    private String type;

    private int position;
}
