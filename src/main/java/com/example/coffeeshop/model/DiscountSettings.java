package com.example.coffeeshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DiscountSettings {
    @Id
    private Long id = 1L; // Завжди один запис
    private Double threshold = 1000d; // Поріг суми (напр. 1000)
    private Double discountPercent = 10d; // % знижки (напр. 10)
    private boolean active; // Поле для активації/деактивації
    
    // Геттери та сеттери
}