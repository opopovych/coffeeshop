package com.example.coffeeshop.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CoffeeBeanDto {

        private String name;
        private String roastLevel;
        private String bitterness;
        private String composition;
        private String intensity;
        private Double price;
        private String photoPath;
        private String brand;
        private String weight;

}

