package com.liverpool.api.dto;

import com.liverpool.api.commons.enums.PaymentMethodEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotBlank(message = "El campo Cliente ID no debe ser nulo ni vacio")
    private String clientId;
    @NotBlank(message = "El campo Nombre de Direccion de Envio no debe ser nulo ni vacio")
    private String shippingAddressName;
    @NotNull(message = "El campo Metodo de pago no debe ser nulo")
    private PaymentMethodEnum paymentMethod;
    @Valid
    private List<ItemOrderRequest> products;
}
