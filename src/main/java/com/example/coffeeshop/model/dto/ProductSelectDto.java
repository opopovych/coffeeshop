package com.example.coffeeshop.model.dto;

import java.math.BigDecimal;

public record ProductSelectDto(
        Long id,
        String brandName,
        String name,
        Double price
) {

    public String getDisplayName() {
        return (brandName != null ? brandName : "Без бренду")
                + " " + name + " (" + price + ")";
    }
}