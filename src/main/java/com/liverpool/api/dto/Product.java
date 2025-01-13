package com.liverpool.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class Product {

    @Id
    private String id;
    @NotBlank(message = "El campo Numero de Serie no debe ser nulo ni vacio")
    private String serialNumber;
    @NotBlank(message = "El campo Descripcion no debe ser nulo ni vacio")
    private String description;
    @NotBlank(message = "El campo URL de la Imagen no debe ser nulo ni vacio")
    private String urlImage;
    @Positive(message = "El campo Precio debe ser mayor a 0")
    private Double price;
    private boolean active;
    private LastModification lastModification;
}
