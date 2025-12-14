package com.learning.backend.product.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.backend.product.dtos.ProductRequestDto;
import com.learning.backend.product.dtos.ProductResponseDto;
import com.learning.backend.product.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void testCreateProduct() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setTitle("Test Product");
        requestDto.setPrice(100.0);
        requestDto.setCategory("Electronics");

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1);
        responseDto.setTitle("Test Product");
        responseDto.setPrice(100.0);
        responseDto.setCategory("Electronics");

        when(productService.create(any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Product"));
    }

    @Test
    @WithMockUser
    public void testGetAllProducts() throws Exception {
        ProductResponseDto p1 = new ProductResponseDto();
        p1.setId(1);
        p1.setTitle("P1");

        ProductResponseDto p2 = new ProductResponseDto();
        p2.setId(2);
        p2.setTitle("P2");

        List<ProductResponseDto> products = Arrays.asList(p1, p2);

        when(productService.getAll()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("P1"));
    }

    @Test
    @WithMockUser
    public void testGetProductById() throws Exception {
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1);
        responseDto.setTitle("Test Product");

        when(productService.getById(1)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Product"));
    }

    @Test
    @WithMockUser
    public void testUpdateProduct() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setTitle("Updated Product");

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1);
        responseDto.setTitle("Updated Product");

        when(productService.update(eq(1), any(ProductRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Product"));
    }
}
