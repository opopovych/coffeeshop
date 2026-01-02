package com.example.coffeeshop.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItem {
    private Long coffeeId;
    private String name;
    private String brand;
    private Double price;
    private Integer quantity;
    private String photoPath;
    private String roastLevel;
    private String bitterness;
    private String composition;
    private String intensity;
    private String weight;
}
