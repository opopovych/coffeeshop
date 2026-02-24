package com.example.coffeeshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ShopSettings {
    @Id
    private Long id = 1L; // Завжди один запис
    private String vendorName; // Назва (напр. ФОП Попович О.І.)
    private String taxNumber;  // ІПН
    private String phoneNumber; // Телефон
    private String shopAddress; // Адреса або опис (Група 2)
}