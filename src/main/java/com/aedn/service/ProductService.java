package com.aedn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import com.aedn.common.Base62Encoder;
import com.aedn.dto.ProductDto;
import com.aedn.dto.ReqProductDto;
import com.aedn.entity.Category;
import com.aedn.entity.Product;
import com.aedn.entity.ProductPicture;
import com.aedn.entity.User;
import com.aedn.exception.ProductNotFoundException;
import com.aedn.exception.UserNotFoundException;
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
    final Slugify slg = Slugify.builder().build();

    @Value("${s3.endpoint.public:}")
    private String s3Endpoint;

    @Transactional
    public ProductDto createProduct(UUID userId, ReqProductDto dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User Not Found, User might be already deleted or your session is invalid"));

        Product product = new Product();
        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setUser(user);
        product.setQuantity(dto.getQuantity());
        product = productRepository.save(product);
        String shortlink = Base62Encoder.encode(product.getId());
        String slug = slg.slugify(dto.getTitle()) + "-" + shortlink;
        product.setUrlSlug(slug);
        product.setPictures(insertProductPictures(dto.getPictureUrls(), product));
        product.setCategory(getCategory(dto.getCategoryName(), user));
        product.setIsActive(dto.getIsActive());
        return createProductDtoFromEntity(productRepository.save(product));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(product -> createProductDtoFromEntity(product))
            .toList();
    }


    @Transactional
    public ProductDto editProduct(UUID userId, Long productId, ReqProductDto dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User Not Found, User might be already deleted or your session is invalid"));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        if (!product.getUser().getId().equals(user.getId())) {
            throw new AuthorizationDeniedException("Unauthorized, this resource belong to others");
        }

        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setIsActive(dto.getIsActive());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.getPictures().clear();
        productRepository.flush();
        product.getPictures().addAll(insertProductPictures(dto.getPictureUrls(), product));

        String shortlink = Base62Encoder.encode(product.getId());
        String slug = slg.slugify(dto.getTitle()) + "-" + shortlink;
        product.setUrlSlug(slug);
        Category oldCategory = product.getCategory();
        Category newCategory = getCategory(dto.getCategoryName(), user);
        product.setCategory(newCategory);
        if (!newCategory.getId().equals(oldCategory.getId()) && !productRepository.existsByCategory(oldCategory)) {
            categoryRepository.findById(oldCategory.getId())
                .ifPresent(categoryRepository::delete);
        }
        return createProductDtoFromEntity(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found or already deleted"));
        productRepository.delete(product);
    }

    private List<ProductPicture> insertProductPictures(List<String> pictureUrls, Product product) {
        List<ProductPicture> pictures = new ArrayList<>();
        for (int i = 0; i < pictureUrls.size(); i++) {
            ProductPicture pic = new ProductPicture();
            pic.setProduct(product);
            pic.setPosition(i);
            pic.setUrl(pictureUrls.get(i));
            pictures.add(pic);
        }
        return pictures;
    }

    private Category getCategory(String categoryName, User user) {
        if(categoryName == "" || categoryName == null) {
            return null;
        }
        return categoryRepository
            .findByNameAndUser(categoryName, user)
            .orElseGet(() -> {
                Category newCategory = new Category();
                newCategory.setUser(user);
                newCategory.setName(categoryName);
                return categoryRepository.save(newCategory);
            });

    }

    private ProductDto createProductDtoFromEntity(Product entity) {
        if (this.s3Endpoint.isBlank()) {
            return ProductDto.fromEntity(entity);
        }
        return ProductDto.fromEntity(entity, this.s3Endpoint);
    }

}
