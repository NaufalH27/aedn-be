
package com.aedn.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.aedn.entity.Category;
import com.aedn.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByIsDeletedFalse();
    Optional<Product> findById(UUID id);
    Optional<Product> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByCategory(Category category);
    Optional<Product> findByUrlSlug(String urlSlug);
}
