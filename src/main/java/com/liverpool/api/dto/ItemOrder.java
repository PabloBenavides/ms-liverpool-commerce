package com.liverpool.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrder {
    @Id
    private String id;
    private String productId;
    private String serialNumber;
    private String description;
    private String urlImage;
    private double price;
    private int qty;
}