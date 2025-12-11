package com.learning.backend.product.services.impl;

import com.learning.backend.product.dtos.FilterDto;
import com.learning.backend.product.dtos.ProductRequestDto;
import com.learning.backend.product.dtos.ProductResponseDto;
import com.learning.backend.product.entities.Category;
import com.learning.backend.product.entities.Product;
import com.learning.backend.product.enums.SortingCriteria;
import com.learning.backend.product.repositories.CategoryRepository;
import com.learning.backend.product.repositories.ProductRepository;
import com.learning.backend.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponseDto getById(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        return ProductResponseDto.from(product);
    }

    @Override
    public List<ProductResponseDto> getAll() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        String categoryName = productRequestDto.getCategory();
        Category category = categoryRepository.findByName(categoryName)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(categoryName);
                    return categoryRepository.save(newCategory);
                });

        Product product = productRequestDto.toEntity();
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return ProductResponseDto.from(savedProduct);
    }

    @Override
    public ProductResponseDto update(Integer productId, ProductRequestDto productRequestDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (StringUtils.hasText(productRequestDto.getTitle())) {
            existingProduct.setTitle(productRequestDto.getTitle());
        }
        if (StringUtils.hasText(productRequestDto.getDescription())) {
            existingProduct.setDescription(productRequestDto.getDescription());
        }
        if (productRequestDto.getPrice() != null) {
            existingProduct.setPrice(productRequestDto.getPrice());
        }
        if (StringUtils.hasText(productRequestDto.getImage())) {
            existingProduct.setImageUrl(productRequestDto.getImage());
        }

        if (StringUtils.hasText(productRequestDto.getCategory())) {
            String categoryName = productRequestDto.getCategory();
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        return categoryRepository.save(newCategory);
                    });
            existingProduct.setCategory(category);
        }

        Product savedProduct = productRepository.save(existingProduct);
        return ProductResponseDto.from(savedProduct);
    }

    @Override
    public Page<ProductResponseDto> search(String query, List<FilterDto> filters, SortingCriteria sortCriteria, Integer page, Integer size) {
        Sort sort = Sort.unsorted();
        if (sortCriteria != null) {
            switch (sortCriteria) {
                case PRICE_ASC:
                    sort = Sort.by("price").ascending();
                    break;
                case PRICE_DESC:
                    sort = Sort.by("price").descending();
                    break;
                case NEWEST_FIRST:
                    sort = Sort.by("createdAt").descending(); // Assuming createdAt exists, or use id
                    break;
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findByTitleContaining(query, pageable);
        return products.map(ProductResponseDto::from);
    }
}
