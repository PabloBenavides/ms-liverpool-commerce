package com.liverpool.api.repository;

import com.liverpool.api.dto.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findBySerialNumber(String serialNumber);
}
