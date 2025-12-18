package com.example.coffeeshop.model;

public enum RoastLevel {
    LIGHT("Легке обсмаження"),      // Легке обсмаження
    MEDIUM("Середнє обсмаження"),     // Середнє обсмаження
    MEDIUM_DARK("Середнє темне обсмаження"),     // Середнє обсмаження
    DARK("Темне обсмаження");     // Темне обсмаження

    public final String displayName;

    RoastLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
