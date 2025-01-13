package com.liverpool.api.service;

import com.liverpool.api.dto.Product;
import com.liverpool.api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_Success() {
        Product product = new Product();
        product.setSerialNumber("12345");

        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void createProduct_DuplicateSerialNumber() {
        Product product = new Product();
        product.setSerialNumber("12345");

        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.createProduct(product));
        assertEquals("400 BAD_REQUEST \"Ya existe un producto con el n\u00famero de serie: 12345.\"", exception.getMessage());
    }

    @Test
    void findById_Success() {
        Product product = new Product();
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));

        Product result = productService.findById("1");

        assertNotNull(result);
    }

    @Test
    void findById_NotFound() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.findById("1"));
        assertEquals("404 NOT_FOUND \"Producto no encontrado con ID: 1.\"", exception.getMessage());
    }

    @Test
    void findBySerialNumber_Success() {
        Product product = new Product();
        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(product));

        Product result = productService.findBySerialNumber("12345");

        assertNotNull(result);
    }

    @Test
    void findBySerialNumber_NotFound() {
        when(productRepository.findBySerialNumber(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.findBySerialNumber("12345"));
        assertEquals("404 NOT_FOUND \"Producto no encontrado con n\u00famero de serie: 12345.\"", exception.getMessage());
    }

    @Test
    void getAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(new Product()));

        List<Product> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
    }

    @Test
    void updateProduct_Success() {
        Product product = new Product();
        product.setId("1");

        when(productRepository.existsById(anyString())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(product);

        assertNotNull(result);
    }

    @Test
    void updateProduct_IdNull() {
        Product product = new Product();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.updateProduct(product));
        assertEquals("400 BAD_REQUEST \"El Producto no puede ser actualizado, el ID no debe ir nulo.\"", exception.getMessage());
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(anyString())).thenReturn(true);

        Boolean result = productService.deleteProduct("1");

        assertTrue(result);
        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(anyString())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.deleteProduct("1"));
        assertEquals("404 NOT_FOUND \"Producto no encontrado con ID: 1.\"", exception.getMessage());
    }

    @Test
    void deactivateProduct_Success() {
        Product product = new Product();
        product.setActive(true);

        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.deactivateProduct("1");

        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @Test
    void deactivateProduct_AlreadyInactive() {
        Product product = new Product();
        product.setActive(false);

        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productService.deactivateProduct("1"));
        assertEquals("400 BAD_REQUEST \"El producto ya est\u00e1 inactivo.\"", exception.getMessage());
    }
}
