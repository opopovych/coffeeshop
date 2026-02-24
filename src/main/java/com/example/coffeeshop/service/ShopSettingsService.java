package com.example.coffeeshop.service;

import com.example.coffeeshop.model.ShopSettings;

public interface ShopSettingsService {
    ShopSettings getSettings();
    void saveSettings(ShopSettings settings);
}