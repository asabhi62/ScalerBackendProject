package com.learning.backend.product.services.impl;

import com.learning.backend.product.dtos.FilterDto;
import com.learning.backend.product.dtos.ProductResponseDto;
import com.learning.backend.product.entities.Product;
import com.learning.backend.product.enums.SortingCriteria;
import com.learning.backend.product.repositories.CategoryRepository;
import com.learning.backend.product.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setTitle("Test Product");
        product.setPrice(100.0);
    }

    @Test
    void search_ShouldReturnPagedProducts_WhenQueryIsProvided() {
        // Arrange
        String query = "Test";
        List<FilterDto> filters = Collections.emptyList();
        SortingCriteria sortCriteria = SortingCriteria.PRICE_ASC;
        int page = 0;
        int size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByTitleContaining(eq(query), any(Pageable.class))).thenReturn(productPage);

        // Act
        Page<ProductResponseDto> result = productService.search(query, filters, sortCriteria, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getTitle());
        verify(productRepository).findByTitleContaining(eq(query), any(Pageable.class));
    }

    @Test
    void search_ShouldReturnEmptyPage_WhenNoProductsMatch() {
        // Arrange
        String query = "NonExistent";
        List<FilterDto> filters = Collections.emptyList();
        SortingCriteria sortCriteria = null;
        int page = 0;
        int size = 10;

        Page<Product> emptyPage = new PageImpl<>(Collections.emptyList());

        when(productRepository.findByTitleContaining(eq(query), any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<ProductResponseDto> result = productService.search(query, filters, sortCriteria, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(productRepository).findByTitleContaining(eq(query), any(Pageable.class));
    }
}
