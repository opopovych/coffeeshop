package com.example.coffeeshop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdAt;
    private String customerName;
    private String surName;
    private String phone;
    private String deliveryAddress;
    private BigDecimal totalPrice;
    private Status status;
    private String paymentMethod; // Додайте це поле
    //Поля для нової пошти
    private String city;        // Назва міста
    private String warehouse;   // Назва/номер відділення
    private String comment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
