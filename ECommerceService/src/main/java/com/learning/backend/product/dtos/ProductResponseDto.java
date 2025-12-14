package com.learning.backend.product.dtos;

import com.learning.backend.product.entities.Product;
import lombok.Data;

@Data
public class ProductResponseDto {
    private Integer id;
    private String title;
    private String description;
    private Double price;
    private String image;
    private String category;

    public static ProductResponseDto from(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImageUrl());
        if (product.getCategory() != null) {
            dto.setCategory(product.getCategory().getName());
        }
        return dto;
    }
}
