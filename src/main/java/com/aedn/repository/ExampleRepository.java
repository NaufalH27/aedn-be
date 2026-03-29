package com.aedn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.Example;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, UUID> {
    Optional<Example> findFirstBy();
}
