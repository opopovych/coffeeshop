package com.example.coffeeshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InfoBarSettings {
    @Id
    private Long id = 1L; // Завжди один запис
    private String message;
    private boolean active;
    private String backgroundColor = "#2e2a28"; // Колір за замовчуванням
}