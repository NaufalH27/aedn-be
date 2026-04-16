package com.aedn.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aedn.common.Base62Encoder;
import com.aedn.dto.CreateProductDto;
import com.aedn.dto.EditProductDto;
import com.aedn.dto.ProductDto;
import com.aedn.entity.Product;
import com.aedn.entity.ProductPicture;
import com.aedn.exception.ProductNotFoundException;
import com.aedn.repository.ProductRepository;

import com.github.slugify.Slugify;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    final Slugify slg = Slugify.builder().build();

    public ProductDto createProduct(CreateProductDto dto) {
        Product product = new Product();
        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());

        List<ProductPicture> pictures = new ArrayList<>();
        for (int i = 0; i < dto.getPictureUrls().size(); i++) {
            ProductPicture pic = new ProductPicture();
            pic.setProduct(product);
            pic.setPosition(i);
            pic.setUrl(dto.getPictureUrls().get(i));
            pictures.add(pic);
        }
        product.setPictures(pictures);

        Product flushedProduct = productRepository.saveAndFlush(product);
        String shortlink = Base62Encoder.encode(flushedProduct.getId());
        String slug = slg.slugify(flushedProduct.getTitle()) + "-" + shortlink;
        flushedProduct.setShortlink(shortlink);
        flushedProduct.setUrlSlug(slug);
        return ProductDto.fromEntity(productRepository.save(flushedProduct));
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(product -> ProductDto.fromEntity(product))
            .toList();
    }


    public ProductDto editProduct(Long productId, EditProductDto dto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));

        product.setCurrencyCode(dto.getCurrencyCode());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        List<ProductPicture> pictures = new ArrayList<>();
        for (int i = 0; i < dto.getPictureUrls().size(); i++) {
            ProductPicture pic = new ProductPicture();
            pic.setProduct(product);
            pic.setPosition(i);
            pic.setUrl(dto.getPictureUrls().get(i));
            pictures.add(pic);
        }
        product.setPictures(pictures);

        String shortlink = Base62Encoder.encode(product.getId());
        String slug = slg.slugify(product.getTitle()) + "-" + shortlink;
        product.setShortlink(shortlink);
        product.setUrlSlug(slug);
        return ProductDto.fromEntity(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
    }


}
