package com.liverpool.api.controller;

import com.liverpool.api.dto.*;
import com.liverpool.api.service.OrderNotificationService;
import com.liverpool.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderNotificationService orderNotificationService;

    public OrderController(OrderService orderService, OrderNotificationService orderNotificationService) {
        this.orderService = orderService;
        this.orderNotificationService = orderNotificationService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        orderNotificationService.sendNotificationCreated(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getAllOrdersByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(orderService.getAllOrdersByClientId(clientId));
    }

    @PatchMapping("/{id}/item")
    public ResponseEntity<Order> addItemToOrder(@PathVariable String id, @RequestBody ItemOrderRequest itemOrderRequest) {
        return ResponseEntity.ok(orderService.addItemToOrder(id, itemOrderRequest));
    }

    @PatchMapping("/{id}/shipAddress/{addressName}")
    public ResponseEntity<Order> updateShippingAddress(
            @PathVariable String id, @PathVariable String addressName) {
        return ResponseEntity.ok(orderService.updateShippingAddress(id, addressName));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable String id) {
        Order order = orderService.cancelOrder(id);
        orderNotificationService.sendNotificationCanceled(order);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/onRoute")
    public ResponseEntity<Order> updateToOnRoute(@PathVariable String id) {
        Order order = orderService.updateToOnRoute(id);
        orderNotificationService.sendNotificationOnRoute(order);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<Order> updateToDelivered(@PathVariable String id) {
        Order order = orderService.updateToDelivered(id);
        orderNotificationService.sendNotificationDelivered(order);
        return ResponseEntity.ok(order);
    }
}
