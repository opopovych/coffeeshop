package com.example.coffeeshop.model;

import lombok.Data;

@Data
public class ProductData {
    private String name;
    private double price;

    public ProductData(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
