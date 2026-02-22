package com.example.coffeeshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class OriginCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 3000)
    private String history;

}
