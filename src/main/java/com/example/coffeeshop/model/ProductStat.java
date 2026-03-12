package com.example.coffeeshop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ProductStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private CoffeeBean product;

    private Long totalSold = 0L; // Загальна кількість
    private Double totalRevenue = 0.0; // Загальна сума виручки
    
    // Можна додати дату останнього продажу
    private LocalDateTime lastSaleDate;
}