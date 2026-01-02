package com.example.coffeeshop.model;

import jakarta.persistence.*;
import lombok.*;
@Data
@Entity
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 3000)
    private String history;

    private String photoPath;
}
