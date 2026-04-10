
package com.aedn.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.aedn.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
