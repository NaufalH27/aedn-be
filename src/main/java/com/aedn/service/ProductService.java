package com.aedn.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aedn.dto.ProductDto;
import com.aedn.dto.ReqProductDto;
import com.aedn.entity.Category;
import com.aedn.entity.Product;
import com.aedn.entity.ProductPicture;
import com.aedn.entity.User;
import com.aedn.exception.ProductNotFoundException;
import com.aedn.mapper.ProductMapper;
import com.aedn.repository.CategoryRepository;
import com.aedn.repository.ProductRepository;
import com.aedn.repository.UserRepository;
import com.github.slugify.Slugify;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    final Slugify slg = Slugify.builder().build();

    @Transactional
    public ProductDto createProduct(UUID userId, ReqProductDto dto) {
        User userRef = userRepository.getReferenceById(userId);
        Product product = new Product();
        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setUser(userRef);

        product = productRepository.save(product);

        String shortId = product.getId().toString().replace("-", "").substring(0, 8);
        String slug = slg.slugify(dto.getTitle()) + "-" + shortId;
        product.setUrlSlug(slug);

        product.setPictures(createProductPictures(dto.getPictureUrls(), product));
        product.setCategory(resolveCategory(dto.getCategoryName()));
        product.setIsActive(dto.getIsActive());

        return productMapper.dtoFromEntity(productRepository.save(product));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findByIsDeletedFalse()
            .stream()
            .map(product -> productMapper.dtoFromEntity(product))
            .toList();
    }

    public ProductDto getById(UUID id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        return productMapper.dtoFromEntity(product);
    }


    @Transactional
    public ProductDto editProduct(UUID userId, UUID productId, ReqProductDto dto) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setIsActive(dto.getIsActive());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        product.getPictures().clear();
        productRepository.flush();
        product.getPictures().addAll(createProductPictures(dto.getPictureUrls(), product));

        String shortId = product.getId().toString().replace("-", "").substring(0, 8);
        String slug = slg.slugify(dto.getTitle()) + "-" + shortId;
        product.setUrlSlug(slug);

        Category oldCategory = product.getCategory();
        Category newCategory = resolveCategory(dto.getCategoryName());
        product.setCategory(newCategory);
        cleanupUnusedCategory(oldCategory, newCategory);

        return productMapper.dtoFromEntity(product);
    }

    private void cleanupUnusedCategory(Category oldCategory, Category newCategory) {
        if (oldCategory == null) {
            return;
        }

        if (newCategory != null && oldCategory.getId().equals(newCategory.getId())) {
            return;
        }

        boolean stillUsed = productRepository.existsByCategory(oldCategory);

        if (!stillUsed) {
            oldCategory.setIsDeleted(true);
            categoryRepository.save(oldCategory);
        }
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setIsDeleted(true);
        product.setDeletedAt(Instant.now());;
    
        productRepository.save(product);
    }

    private List<ProductPicture> createProductPictures(List<String> pictureUrls, Product product) {
        List<ProductPicture> pictures = new ArrayList<>();
        if (pictureUrls == null) {
            return pictures;
        }
        for (int i = 0; i < pictureUrls.size(); i++) {
            ProductPicture pic = new ProductPicture();
            pic.setProduct(product);
            pic.setPosition(i);
            pic.setUrl(pictureUrls.get(i));
            pictures.add(pic);
        }
        return pictures;
    }

    private Category resolveCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return null;
        }

        String normalizedName = categoryName.trim();

        Category resolvedCategory = categoryRepository.findByName(normalizedName)
            .orElseGet(() -> {
                Category category = new Category();
                category.setName(normalizedName);
                return categoryRepository.save(category);
            });

        if (resolvedCategory.getIsDeleted()) {
            resolvedCategory.setIsDeleted(false);
            categoryRepository.save(resolvedCategory);
        }

        return resolvedCategory;
    }
}
