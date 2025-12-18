package com.example.coffeeshop.model;

public enum Bitterness {
    LOW("Низька гіркота"),        // Низька гіркота
    MEDIUM("Середня гіркота"),     // Середня гіркота
    HIGH("Висока гіркота");       // Висока гіркота

    public final String displayName;

    Bitterness(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
