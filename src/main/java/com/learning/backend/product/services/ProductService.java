package com.learning.backend.product.services;

import com.learning.backend.product.dtos.ProductRequestDto;
import com.learning.backend.product.dtos.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto getById(Integer productId);
    List<ProductResponseDto> getAll();
    ProductResponseDto create(ProductRequestDto productRequestDto);
    ProductResponseDto update(Integer productId, ProductRequestDto productRequestDto);
}
