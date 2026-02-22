package com.example.coffeeshop.service;

import com.example.coffeeshop.model.DiscountSettings;

public interface DiscountService {
    DiscountSettings getSettings();
    void saveSettings(Double threshold, Double percent, boolean active);
}
