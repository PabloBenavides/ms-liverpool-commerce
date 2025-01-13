package com.liverpool.api.service;

import com.liverpool.api.dto.Order;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class OrderNotificationService {

    private final EmailService emailService;

    public OrderNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNotificationCreated(Order order){
        Context context = new Context();
        context.setVariable("clientName", order.getFirstName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", order.getOrderDate());
        context.setVariable("total", order.getOrderTotal());
        context.setVariable("shippingAddress", order.getShippingAddress());
        context.setVariable("paymentMethod", order.getPaymentMethod());
        context.setVariable("products", order.getProducts());
        emailService.sendEmail(
                order.getEmail(),
                "Gracias por tu compra - Liverpool",
                "orderConfirmation",
                context
        );
    }

    public void sendNotificationCanceled(Order order){
        Context context = new Context();
        context.setVariable("clientName", order.getFirstName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", order.getOrderDate());
        context.setVariable("total", order.getOrderTotal());
        context.setVariable("shippingAddress", order.getShippingAddress());
        context.setVariable("paymentMethod", order.getPaymentMethod());
        context.setVariable("products", order.getProducts());
        emailService.sendEmail(
                order.getEmail(),
                "Tu compra ha sido cancelada - Liverpool",
                "orderCanceled",
                context
        );
    }

    public void sendNotificationOnRoute(Order order){
        Context context = new Context();
        context.setVariable("clientName", order.getFirstName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", order.getOrderDate());
        context.setVariable("deliveryDate", order.getDeliveryDate());
        context.setVariable("total", order.getOrderTotal());
        context.setVariable("shippingAddress", order.getShippingAddress());
        context.setVariable("paymentMethod", order.getPaymentMethod());
        context.setVariable("orderStatus", order.getOrderStatus().getStatusText());
        context.setVariable("products", order.getProducts());
        emailService.sendEmail(
                order.getEmail(),
                "Tu compra va en camino - Liverpool",
                "orderChangeStatus",
                context
        );
    }

    public void sendNotificationDelivered(Order order){
        Context context = new Context();
        context.setVariable("clientName", order.getFirstName());
        context.setVariable("orderId", order.getId());
        context.setVariable("orderDate", order.getOrderDate());
        context.setVariable("deliveryDate", order.getDeliveryDate());
        context.setVariable("total", order.getOrderTotal());
        context.setVariable("shippingAddress", order.getShippingAddress());
        context.setVariable("paymentMethod", order.getPaymentMethod());
        context.setVariable("orderStatus", order.getOrderStatus().getStatusText());
        context.setVariable("products", order.getProducts());
        emailService.sendEmail(
                order.getEmail(),
                "Tu compra ha sido entregada - Liverpool",
                "orderChangeStatus",
                context
        );
    }


}
