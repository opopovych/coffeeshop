package com.example.coffeeshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long coffeeId;
    private String coffeeBrand;
    private String name;
    private Integer quantity;
    private Double price;
    private String weight;
    private String productFormat;
    private String capsuleSystem;
    private String capsuleCount;


    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
}
