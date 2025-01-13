# Liverpool Commerce API

## Descripción General
La **Liverpool Commerce API** es una Prueba Tecnica para Java Backend implementando una API RESTful para gestionar clientes, órdenes y productos de un ecommerce. Está construida con Spring Boot y MongoDB como base de datos. Esta API incluye endpoints para crear, actualizar, obtener y eliminar clientes, órdenes y productos.

## Tecnologías Utilizadas

- **Spring Boot 3.4.1**
- **Java 21**
- **MongoDB**
- **Jakarta Mail**
- **Thymeleaf**
- **Gradle**

## Endpoints de la API

Para la documentación completa de la API, consulta el archivo Swagger o Postman Collection disponible en el proyecto: /documentacion.
Tambien en la carpeta /documentacion viene un archivo con request disponibles para usar con la base de datos existente. Especialmente las ordenes.

### Ejemplos de Endpoints

#### Clientes

- **Crear cliente**
    - **POST** `/clients`
      - Ejemplo de payload:
        ```json
        {
          "firstName": "Ricardo",
          "middleName": "Lino",
          "paternalLastName": "García",
          "maternalLastName": "López",
          "email": "rlgarcial@liverpool.com.mx",
            "addresses": [
                {
                    "addressName": "Casa Principal",
                    "street": "Calle Hidalgo",
                    "exteriorNumber": "123",
                    "interiorNumber": "4B",
                    "neighborhood": "Zona Centro",
                    "city": "Ciudad Victoria",
                    "state": "Tamaulipas",
                    "postalCode": "87000",
                    "country": "México"
                },
                {
                    "addressName": "Oficina",
                    "street": "Calle 16",
                    "exteriorNumber": "456",
                    "interiorNumber": "2A",
                    "neighborhood": "Guerrero",
                    "city": "Reynosa",
                    "state": "Tamaulipas",
                    "postalCode": "88500",
                    "country": "México"
                }
            ]
        }
      ```

- **Obtener cliente por ID**
    - **GET** `/clients/{id}`

- **Eliminar cliente**
    - **DELETE** `/clients/{id}`

## Ejecución del Proyecto

1. Clona el repositorio.
2. Ejecuta el comando:

   ```bash
   ./gradlew bootRun
   ```

3. La aplicación estará disponible en: [http://localhost:7001](http://localhost:7001)

## Pruebas

Ejecuta las pruebas utilizando:

```bash
./gradlew test
