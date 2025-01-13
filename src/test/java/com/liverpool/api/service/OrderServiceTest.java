package com.liverpool.api.service;

import com.liverpool.api.commons.enums.OrderStatusEnum;
import com.liverpool.api.commons.enums.PaymentMethodEnum;
import com.liverpool.api.dto.*;
import com.liverpool.api.repository.OrderRepository;
import com.liverpool.api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_Success() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setClientId("1");
        orderRequest.setProducts(List.of(new ItemOrderRequest("12345", 2)));
        orderRequest.setShippingAddressName("HOME");
        orderRequest.setPaymentMethod(PaymentMethodEnum.CREDIT_CARD);

        Client client = new Client();
        client.setId("1");
        client.setEmail("test@example.com");
        client.setFirstName("John");
        client.setMiddleName("A.");
        client.setPaternalLastName("Doe");
        client.setMaternalLastName("Smith");
        Address address = new Address();
        address.setAddressName("HOME");
        client.setAddresses(List.of(address));

        Product product = new Product();
        product.setId("12345");
        product.setPrice(100.0);
        product.setSerialNumber("12345");
        product.setDescription("Test Product");
        product.setActive(true);

        when(clientService.findById(anyString())).thenReturn(client);
        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(orderRequest);

        assertNotNull(result);
        assertEquals(200.0, result.getOrderTotal());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_InvalidPaymentMethod() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setClientId("1");
        orderRequest.setProducts(List.of(new ItemOrderRequest("12345", 2)));
        orderRequest.setShippingAddressName("HOME");
        orderRequest.setPaymentMethod(null);

        Client client = new Client();
        Address address = new Address();
        address.setAddressName("HOME");
        client.setAddresses(List.of(address));

        when(clientService.findById(anyString())).thenReturn(client);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.createOrder(orderRequest));

        assertNotNull(exception);
    }

    @Test
    void getOrderById_Success() {
        Order order = new Order();
        when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById("1");

        assertNotNull(result);
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.getOrderById("1"));

        assertTrue(exception.getMessage().contains("Orden no encontrada con ID"));
    }

    @Test
    void addItemToOrder_Success() {
        Order order = new Order();
        order.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order.setProducts(new ArrayList<>());

        Product product = new Product();
        product.setId("12345");
        product.setSerialNumber("12345");
        product.setPrice(100.0);
        product.setActive(true);

        ItemOrderRequest itemOrderRequest = new ItemOrderRequest("12345", 2);

        when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));
        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.addItemToOrder("1", itemOrderRequest);

        assertNotNull(result);
        assertEquals(200.0, result.getOrderTotal());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void addItemToOrder_OrderNotInProcess() {
        Order order = new Order();
        order.setOrderStatus(OrderStatusEnum.DELIVERED);

        when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

        ItemOrderRequest itemOrderRequest = new ItemOrderRequest("12345", 2);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> orderService.addItemToOrder("1", itemOrderRequest));

        assertTrue(exception.getMessage().contains("No se puede modificar la orden porque no est\u00e1 en estado IN_PROCESS"));
    }
}
