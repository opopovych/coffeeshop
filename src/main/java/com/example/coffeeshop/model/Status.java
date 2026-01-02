package com.example.coffeeshop.model;

import lombok.Getter;

@Getter
public enum Status {
    WAITING("Очікує"),
    AT_WORK("В роботі"),
    DONE("Виконано");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

}
