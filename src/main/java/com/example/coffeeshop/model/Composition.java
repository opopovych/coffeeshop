package com.example.coffeeshop.model;

public enum Composition {
    ARABICA_100("100% Арабіка"),   // 100% Арабіка
    ROBUSTA_100("100% Робуста"),   // 100% Робуста
    MIX_70_30("Купаж 70% арабіка, 30% робуста"),     // Купаж 70% арабіка, 30% робуста
    MIX_80_20("Купаж 80% арабіка, 20% робуста"),     // Купаж 80/20
    MIX_60_40("Купаж 60% арабіка, 40% робуста"),     // Купаж 60/40
    MIX("Купаж арабіки та робусти");

    public final String displayName;

    Composition(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
