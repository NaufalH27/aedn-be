package com.aedn.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.ExampleEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class ExampleRepository {
    
    // @PersistenceContext
    // private final EntityManager entityManager;

    // public IndexRepository(EntityManager entityManager) {
    //     this.entityManager = entityManager;
    // }

    public ExampleEntity example() {
        ExampleEntity e = new ExampleEntity();
        e.setId(UUID.randomUUID());
        e.setExampleString("Test String");
        e.setExampleInteger(42);
        e.setExampleFloat(3.14f);
        return e;
    }
}
