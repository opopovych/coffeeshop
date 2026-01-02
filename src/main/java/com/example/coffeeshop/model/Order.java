package com.example.coffeeshop.model;

import jakarta.persistence.*;
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

    private String customerName;
    private String phone;
    private String deliveryAddress;
    private Double totalPrice;
    private Status status;
    //Поля для нової пошти
    private String city;        // Назва міста
    private String warehouse;   // Назва/номер відділення

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}
