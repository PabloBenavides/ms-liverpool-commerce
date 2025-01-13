package com.liverpool.api.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    private String id;
    @NotBlank(message = "El campo Nombre de Direccion no debe ser nulo ni vacio")
    private String addressName;
    @NotBlank(message = "El campo Calle no debe ser nulo ni vacio")
    private String street;
    @NotBlank(message = "El campo Numero Exterior no debe ser nulo ni vacio")
    private String exteriorNumber;
    private String interiorNumber;
    private String neighborhood;
    @NotBlank(message = "El campo Ciudad no debe ser nulo ni vacio")
    private String city;
    @NotBlank(message = "El campo Estado no debe ser nulo ni vacio")
    private String state;
    @NotBlank(message = "El campo Codigo Postall no debe ser nulo ni vacio")
    private String postalCode;
    @NotBlank(message = "El campo Pais no debe ser nulo ni vacio")
    private String country;

    @Override
    public String toString() {
        return String.format(
                "%s :  %s %s - %s, %s %s, %s, %s, %s",
                addressName, street, exteriorNumber, interiorNumber, neighborhood, city, state, postalCode, country
        );
    }
}
