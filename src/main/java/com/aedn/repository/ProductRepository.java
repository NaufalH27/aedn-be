
package com.aedn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.Category;
import com.aedn.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findById(UUID id);
    boolean existsByCategory(Category category);
    Optional<Product> findByUrlSlug(String urlSlug);
}
