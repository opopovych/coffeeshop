package com.example.coffeeshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReportDto {
    private String brand;
    private String name;
    private String weight;
    private long totalQuantity;
}