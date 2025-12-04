package com.learning.backend.product.dtos;

import com.learning.backend.product.entities.Product;
import lombok.Data;

@Data
public class ProductRequestDto {
    private String title;
    private String description;
    private Double price;
    private String image;
    private String category;

    public Product toEntity() {
        Product product = new Product();
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setImageUrl(this.image);
        return product;
    }
}
