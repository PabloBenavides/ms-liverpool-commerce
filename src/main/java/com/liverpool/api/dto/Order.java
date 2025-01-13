package com.liverpool.api.dto;

import com.liverpool.api.commons.enums.OrderStatusEnum;
import com.liverpool.api.commons.enums.PaymentMethodEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private OrderStatusEnum orderStatus;
    @NotBlank(message = "El campo Cliente ID no debe ser nulo ni vacio")
    private String clientId;
    @NotBlank(message = "El campo Nombre no debe ser nulo ni vacio")
    private String firstName;
    private String middleName;
    @NotBlank(message = "El campo Apellido Paterno no debe ser nulo ni vacio")
    private String paternalLastName;
    @NotBlank(message = "El campo Apellido Materno no debe ser nulo ni vacio")
    private String maternalLastName;
    @NotBlank(message = "El campo Correo Electronico no debe ser nulo ni vacio")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El campo Correo Electronico debe ser un email válido"
    )
    private String email;
    @Valid
    private Address shippingAddress;
    @Valid
    private List<ItemOrder> products;
    private double orderTotal;
    private PaymentMethodEnum paymentMethod;
    private LastModification lastModification;
}
