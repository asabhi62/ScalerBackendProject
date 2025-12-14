package com.learning.backend.product.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.backend.product.dtos.ProductRequestDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Integer createdProductId;

    @Test
    @Order(1)
    @WithMockUser
    public void testCreateProductFlow() throws Exception {
        // 1. Create Product
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setTitle("Integration Test Product");
        requestDto.setDescription("Description for integration test");
        requestDto.setPrice(500.0);
        requestDto.setCategory("Integration Category");
        requestDto.setImage("http://example.com/image.png");

        MvcResult result = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Test Product"))
                .andExpect(jsonPath("$.category").value("Integration Category"))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        createdProductId = objectMapper.readTree(responseString).get("id").asInt();
        System.out.println("Created Product ID: " + createdProductId);
    }

    @Test
    @Order(2)
    @WithMockUser
    public void testGetProductByIdFlow() throws Exception {
        // 2. Get Product By ID
        mockMvc.perform(get("/api/v1/products/" + createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProductId))
                .andExpect(jsonPath("$.title").value("Integration Test Product"));
    }

    @Test
    @Order(3)
    @WithMockUser
    public void testGetAllProductsFlow() throws Exception {
        // 3. Get All Products
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + createdProductId + ")]").exists());
    }
}
