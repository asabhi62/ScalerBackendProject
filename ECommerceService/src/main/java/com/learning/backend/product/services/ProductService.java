package com.learning.backend.product.services;

import com.learning.backend.product.dtos.FilterDto;
import com.learning.backend.product.dtos.ProductRequestDto;
import com.learning.backend.product.dtos.ProductResponseDto;
import com.learning.backend.product.enums.SortingCriteria;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponseDto getById(Integer productId);
    List<ProductResponseDto> getAll();
    ProductResponseDto create(ProductRequestDto productRequestDto);
    ProductResponseDto update(Integer productId, ProductRequestDto productRequestDto);
    Page<ProductResponseDto> search(String query, List<FilterDto> filters, SortingCriteria sortCriteria, Integer page, Integer size);
}
