package com.example.coffeeshop.model;

public enum Intensity {
    SOFT("М'яка"),       // М'яка
    BALANCED("Збалансована"),   // Збалансована
    STRONG("Міцна");      // Міцна

    public final String displayName;

    Intensity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
