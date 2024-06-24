package com.shopping_microservices.product_service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;
import com.shopping_microservices.product_service.dto.ProductRequest;
import com.shopping_microservices.product_service.dto.ProductResponse;
import com.shopping_microservices.product_service.model.Product;
import com.shopping_microservices.product_service.repository.ProductRepository;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @AfterEach
    void cleanUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequest productRequest = getProductRequest(); 
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productRequestString))
            .andExpect(status().isCreated());
        Assertions.assertTrue(productRepository.findAll().size()==1);
    }

    // TODO: Add test for getting list of multiple products
    // TODO: Clean up having to create new product
    @Test
    void shouldGetProduct() throws Exception {
        Product product = Product.builder()
            .id("product1")
            .name("Test Product")
            .description("Test Product")
            .price(BigDecimal.valueOf(1000))
            .build();
        productRepository.save(product);
        ProductResponse productResponse = getProductResponse(); 
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
            .andExpect(status().isOk())
            .andReturn();
        String json = result.getResponse().getContentAsString();
        List<ProductResponse> products = objectMapper.readValue(json, new TypeReference<>(){});
        assertThat(products).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsExactly(productResponse);
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = Product.builder()
            .id("product1")
            .name("Test Product")
            .description("Test Product")
            .price(BigDecimal.valueOf(1000))
            .build();
        productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/product").param("productId", "product1"))
            .andExpect(status().isOk());
        Assertions.assertTrue(productRepository.findAll().size()==0);
    }

    // TODO: Remove hardcoding of values
    private ProductResponse getProductResponse() {
        return ProductResponse.builder()
            .name("Test Product")
            .description("Test Product")
            .price(BigDecimal.valueOf(1000))
            .build();
    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder()
            .name("Test Product")
            .description("Test Product")
            .price(BigDecimal.valueOf(1000))
            .build();
    }
}
