package com.liverpool.api.repository;

import com.liverpool.api.dto.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findAllByClientId(String clientId);
}
