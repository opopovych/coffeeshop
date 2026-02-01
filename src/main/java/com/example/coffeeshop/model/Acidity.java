package com.example.coffeeshop.model;

import lombok.Getter;

@Getter
public enum Acidity {
    LOW("Низька кислотність"),        // Низька кислотність
    MEDIUM("Середня кислотність"),     // Середня кислотність
    HIGH("Висока кислотність");       // Висока кислотність

    public final String displayName;

    Acidity(String displayName) {
        this.displayName = displayName;
    }

}
