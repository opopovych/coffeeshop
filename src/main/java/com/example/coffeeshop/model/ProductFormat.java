package com.example.coffeeshop.model;

import lombok.Getter;

@Getter
public enum ProductFormat {
    GRAIN("У зернах"),  // Зерно
    GROUND("Мелена"),
    CAPSULE("Капсули");

    final String displayName;

    ProductFormat(String displayName) {
        this.displayName = displayName;
    }
}