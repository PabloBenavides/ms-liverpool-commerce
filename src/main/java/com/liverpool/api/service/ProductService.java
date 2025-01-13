package com.liverpool.api.service;

import com.liverpool.api.dto.Product;
import com.liverpool.api.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.liverpool.api.commons.enums.ActionsEnum.CREATED;
import static com.liverpool.api.commons.enums.ActionsEnum.UPDATED;
import static com.liverpool.api.commons.lastmodification.LastModificationCreator.createLastModification;

@Service
public class ProductService {

    private static final String MSG_PRODUCT_NOT_FOUND = "Producto no encontrado con ID: %s.";
    private static final String MSG_PRODUCT_NOT_FOUND_BY_SERIAL = "Producto no encontrado con número de serie: %s.";
    private static final String MSG_ID_NULL = "El Producto no puede ser actualizado, el ID no debe ir nulo.";
    private static final String MSG_ALREADY_INACTIVE = "El producto ya está inactivo.";
    private static final String MSG_SERIAL_NUMBER_DUPLICATED = "Ya existe un producto con el número de serie: %s.";
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        boolean exists = productRepository.findBySerialNumber(product.getSerialNumber()).isPresent();

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(MSG_SERIAL_NUMBER_DUPLICATED, product.getSerialNumber())
            );
        }

        product.setLastModification(createLastModification(CREATED.getActionText(), "system"));
        return productRepository.save(product);
    }


    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format(MSG_PRODUCT_NOT_FOUND, id)));
    }

    public Product findBySerialNumber(String serialNumber) {
        return productRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, String.format(MSG_PRODUCT_NOT_FOUND_BY_SERIAL, serialNumber)));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        if (product.getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    MSG_ID_NULL
            );
        }

        if (!productRepository.existsById(product.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format(MSG_PRODUCT_NOT_FOUND, product.getId())
            );
        }

        product.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return productRepository.save(product);
    }

    public Boolean deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, String.format(MSG_PRODUCT_NOT_FOUND, id));
        }

        productRepository.deleteById(id);
        return true;
    }

    public Product deactivateProduct(String id) {
        Product product = findById(id);

        if (!product.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, MSG_ALREADY_INACTIVE);
        }

        product.setActive(false);
        product.setLastModification(createLastModification(UPDATED.getActionText(), "system"));
        return productRepository.save(product);
    }
}
