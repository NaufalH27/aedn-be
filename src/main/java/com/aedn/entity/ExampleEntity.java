package com.aedn.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @Entity
public class ExampleEntity {
    
    // @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    // @Column(nullable=false)
    private String exampleString;
    private int exampleInteger;
    private float exampleFloat;
}
