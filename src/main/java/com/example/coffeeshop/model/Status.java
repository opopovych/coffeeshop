package com.example.coffeeshop.model;

public enum Status {
    WHAITING("Очікує"),
    AT_WORK("В роботі"),
    DONE("Виконано");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
