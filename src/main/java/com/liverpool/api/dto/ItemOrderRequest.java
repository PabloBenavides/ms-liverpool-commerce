package com.liverpool.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequest {
    @NotBlank(message = "El campo Numero de Serie no debe ser nulo ni vacio")
    private String serialNumber;
    @Positive(message = "El campo Precio debe ser mayor a 0")
    private int qty;
}
