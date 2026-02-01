package com.example.coffeeshop.model;

import lombok.Data;

@Data
public class ProductInfo {
    private String sku;
    private String name;

    public ProductInfo(String sku, String name) {
        this.sku = sku;
        this.name = name;
    }
}