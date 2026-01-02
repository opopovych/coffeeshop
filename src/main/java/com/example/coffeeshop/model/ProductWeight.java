package com.example.coffeeshop.model;

import lombok.Getter;

@Getter
public enum ProductWeight {
    SMALL(250, "250 г"),
    MEDIUM(500, "500 г"),
    LARGE(1000, "1 кг"),
    EXTRA_LARGE(3000, "3 кг");

    private final int grams;
    private final String displayName;

    ProductWeight(int grams, String displayName) {
        this.grams = grams;
        this.displayName = displayName;
    }

}