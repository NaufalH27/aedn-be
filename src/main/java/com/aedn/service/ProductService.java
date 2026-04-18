package com.aedn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return ProductDto.fromEntity(productRepository.save(product));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(product -> ProductDto.fromEntity(product))
            .toList();
    }


    public ProductDto editProduct(UUID userId, Long productId, ReqProductDto dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User Not Found, User might be already deleted or your session is invalid"));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        if (product.getUser().getId() != user.getId()) {
            throw new AuthorizationDeniedException("Unauthorized, this resource belong to others");
        }

        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setPictures(insertProductPictures(dto.getPictureUrls(), product));
        product.setCategory(getCategory(dto.getCategoryName(), user));
        return ProductDto.fromEntity(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
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
        return categoryRepository
            .findByNameAndUser(categoryName, user)
            .orElseGet(() -> {
                Category newCategory = new Category();
                newCategory.setUser(user);
                newCategory.setName(categoryName);
                return categoryRepository.save(newCategory);
            });

    }

}
