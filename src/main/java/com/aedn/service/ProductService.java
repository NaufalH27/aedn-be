package com.aedn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aedn.dto.CreateProductDto;
import com.aedn.dto.EditProductDto;
import com.aedn.dto.ProductDto;
import com.aedn.entity.Product;
import com.aedn.entity.ProductPicture;
import com.aedn.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto createProduct(CreateProductDto dto) {
        Product product = new Product();
        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        // TODO: generate slug
        product.setUrlSlug(null);

        List<ProductPicture> pictures = new ArrayList<>();
        for (int i = 0; i < dto.getPictureUrls().size(); i++) {
            ProductPicture pic = new ProductPicture();
            pic.setProduct(product);
            pic.setPosition(i);
            pic.setUrl(dto.getPictureUrls().get(i));
            pictures.add(pic);
        }

        product.setPictures(pictures);
        Product savedProduct = productRepository.save(product);
        return ProductDto.fromEntity(savedProduct);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(product -> ProductDto.fromEntity(product))
            .toList();
    }

    public ProductDto editProduct(EditProductDto dto) {
        return null;
    }

    public void deleteProduct(Long id) {
    }


}
