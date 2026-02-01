package com.example.coffeeshop.model;

import lombok.Data;

@Data
public class SyncReport {
    private int updated;
    private int deactivated;

    public SyncReport(int updated, int deactivated) {
        this.updated = updated;
        this.deactivated = deactivated;
    }

}