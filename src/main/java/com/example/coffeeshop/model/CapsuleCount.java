package com.example.coffeeshop.model;

public enum CapsuleCount {

    C8(8),
    C10(10),
    C12(12),
    C16(16),
    C18(18),
    C21(21),
    C30(30),
    C36(36),
    C48(48),
    C50(50),
    C80(80),
    C100(100),
    C150(150);

    private final Integer value;

    CapsuleCount(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}