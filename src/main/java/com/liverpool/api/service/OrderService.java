package com.liverpool.api.service;

import com.liverpool.api.commons.enums.OrderStatusEnum;
import com.liverpool.api.commons.enums.PaymentMethodEnum;
import com.liverpool.api.dto.*;
import com.liverpool.api.repository.OrderRepository;
import com.liverpool.api.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.liverpool.api.commons.enums.ActionsEnum.CREATED;
import static com.liverpool.api.commons.enums.ActionsEnum.UPDATED;
import static com.liverpool.api.commons.lastmodification.LastModificationCreator.createLastModification;

@Service
public class OrderService {

    private static final String MSG_ORDER_NOT_FOUND = "Orden no encontrada con ID: %s.";
    private static final String MSG_NO_ITEMS = "La orden no contiene items.";
    private static final String MSG_PRODUCT_NOT_FOUND = "Producto no encontrado en el catálogo: %s.";
    private static final String MSG_PRODUCT_INACTIVE = "Producto inactivo, no se puede añadir: %s.";
    private static final String MSG_INVALID_ORDER_STATE = "No se puede modificar la orden porque no está en estado IN_PROCESS.";
    private static final String MSG_INVALID_STATUS_TRANSITION = "Transición de estado no permitida.";
    private static final String MSG_DUPLICATED_ITEM = "El producto ya existe en la orden.";
    private static final String MSG_SHIPADDRESS_INVALID = "Direccion de Envio invalida";
    private static final String MSG_INVALID_PAYMENT = "El método de pago no es válido";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ClientService clientService;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, ClientService clientService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.clientService = clientService;
    }

    public Order createOrder(OrderRequest order) {
        Client client = clientService.findById(order.getClientId());
        List<ItemOrder> items = validateItems(order.getProducts());
        Address shipAddress = client.getAddresses().stream()
                .filter(address -> order.getShippingAddressName().toUpperCase().equals(address.getAddressName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_SHIPADDRESS_INVALID));
        if (Arrays.stream(PaymentMethodEnum.values())
                .noneMatch(paymentMethod -> paymentMethod.name().equals(order.getPaymentMethod().name()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_INVALID_PAYMENT);
        }

        Order orderToSave = Order.builder()
                .orderDate(LocalDateTime.now())
                .deliveryDate(LocalDateTime.now().plusDays(5))
                .clientId(client.getId())
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .middleName(client.getMiddleName())
                .paternalLastName(client.getPaternalLastName())
                .maternalLastName(client.getMaternalLastName())
                .shippingAddress(shipAddress)
                .products(items)
                .orderTotal(calculateTotal(items))
                .paymentMethod(order.getPaymentMethod())
                .orderStatus(OrderStatusEnum.IN_PROCESS)
                .lastModification(createLastModification(CREATED.getActionText(), "system"))
                .build();

        return orderRepository.save(orderToSave);
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(MSG_ORDER_NOT_FOUND, orderId)));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersByClientId(String clientId) {
        return orderRepository.findAllByClientId(clientId);
    }

    public Order addItemToOrder(String orderId, ItemOrderRequest itemOrderRequest) {
        Order order = getOrderById(orderId);

        if (!order.getOrderStatus().equals(OrderStatusEnum.IN_PROCESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_INVALID_ORDER_STATE);
        }

        ItemOrder itemOrder = validateItem(itemOrderRequest);

        boolean itemExists = order.getProducts().stream()
                .anyMatch(item -> item.getProductId().equals(itemOrder.getProductId()));

        if (itemExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, MSG_DUPLICATED_ITEM);
        }

        order.getProducts().add(itemOrder);
        order.setOrderTotal(calculateTotal(order.getProducts()));
        order.setLastModification(createLastModification(UPDATED.getActionText(), "system"));

        return orderRepository.save(order);
    }

    public Order updateShippingAddress(String orderId, String newShippingAddress) {
        Order order = getOrderById(orderId);
        Client client = clientService.findById(order.getClientId());
        Address shipAddress = client.getAddresses().stream()
                .filter(address -> newShippingAddress.toUpperCase().equals(address.getAddressName()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_SHIPADDRESS_INVALID));

        order.setShippingAddress(shipAddress);
        order.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return orderRepository.save(order);
    }

    public Order cancelOrder(String orderId) {
        Order order = getOrderById(orderId);
        order.setOrderStatus(OrderStatusEnum.CANCELED);
        order.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return orderRepository.save(order);
    }

    public Order updateToOnRoute(String orderId) {
        Order order = getOrderById(orderId);

        if (!order.getOrderStatus().equals(OrderStatusEnum.IN_PROCESS)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_INVALID_STATUS_TRANSITION);
        }

        order.setOrderStatus(OrderStatusEnum.ON_ROUTE);
        order.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return orderRepository.save(order);
    }

    public Order updateToDelivered(String orderId) {
        Order order = getOrderById(orderId);

        if (!order.getOrderStatus().equals(OrderStatusEnum.ON_ROUTE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_INVALID_STATUS_TRANSITION);
        }

        order.setOrderStatus(OrderStatusEnum.DELIVERED);
        order.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return orderRepository.save(order);
    }

    private List<ItemOrder> validateItems(List<ItemOrderRequest> itemsRequest) {
        if (itemsRequest.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_NO_ITEMS);
        }

        return itemsRequest.stream()
                .map(this::validateItem)
                .toList();
    }

    private ItemOrder validateItem(ItemOrderRequest item) {
        Product product = productRepository.findBySerialNumber(item.getSerialNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format(MSG_PRODUCT_NOT_FOUND, item.getSerialNumber())));

        if (!product.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(MSG_PRODUCT_INACTIVE, item.getSerialNumber()));
        }

        return ItemOrder.builder()
                .qty(item.getQty())
                .productId(product.getId())
                .serialNumber(product.getSerialNumber())
                .price(product.getPrice())
                .description(product.getDescription())
                .urlImage(product.getUrlImage())
                .build();
    }

    private double calculateTotal(List<ItemOrder> items) {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQty()).sum();
    }
}
