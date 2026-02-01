package com.example.coffeeshop.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DetailedSyncReport {
    private List<ProductInfo> updated = new ArrayList<>();
    private List<ProductInfo> missingInDatabase = new ArrayList<>(); // Новинки з файлу
    private List<ProductInfo> missingInFile = new ArrayList<>();     // Архівні з бази

    // Геттери
}