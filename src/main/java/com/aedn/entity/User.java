package com.aedn.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    
    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password; // hashed 

    private String fullName;

    @Column(nullable=false)
    private Boolean isActive = true;

    @Column(nullable=false)
    private Boolean isVerified = false;

    @Column(nullable=false)
    private Boolean isAdmin = false;

}
