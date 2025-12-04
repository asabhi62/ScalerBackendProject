package com.learning.backend.product.controllers;

import com.learning.backend.product.dtos.ProductRequestDto;
import com.learning.backend.product.dtos.ProductResponseDto;
import com.learning.backend.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productService.create(productRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Integer productId, @RequestBody ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.update(productId, productRequestDto));
    }
}
