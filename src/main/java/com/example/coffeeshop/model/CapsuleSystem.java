package com.example.coffeeshop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


public enum CapsuleSystem {

    NESPRESSO("Nespresso"),
    DOLCE_GUSTO("Dolce Gusto"),
    TASSIMO("Tassimo"),
    SENSEO("Senseo"),
    CAFISSIMO("Cafissimo"),
    ESE("ESE Pods");

    private final String displayName;
    CapsuleSystem(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}