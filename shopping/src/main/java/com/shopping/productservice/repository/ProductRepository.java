package com.shopping.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shopping.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
