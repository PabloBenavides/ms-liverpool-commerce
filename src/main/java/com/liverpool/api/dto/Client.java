package com.liverpool.api.dto;

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
@Document(collection = "clients")
public class Client {

    @Id
    private String id;
    private LocalDateTime creationDate;
    @NotBlank(message = "El campo Nombre no debe ser nulo ni vacio")
    private String firstName;
    private String middleName;
    @NotBlank(message = "El campo Apellido Paterno no debe ser nulo ni vacio")
    private String paternalLastName;
    @NotBlank(message = "El campo Apellido Materno no debe ser nulo ni vacio")
    private String maternalLastName;
    @Valid
    private List<Address> addresses;
    @NotBlank(message = "El campo Correo Electronico no debe ser nulo ni vacio")
    @Pattern(
            regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "El campo Correo Electronico debe ser un email válido"
    )
    private String email;
    private LastModification lastModification;
}
