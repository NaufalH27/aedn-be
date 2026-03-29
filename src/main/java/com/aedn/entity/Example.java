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
public class Example {
    
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable=false)
    private String exampleString;
    private int exampleInteger;
    private float exampleFloat;
}
