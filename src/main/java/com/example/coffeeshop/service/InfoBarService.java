package com.example.coffeeshop.service;

import com.example.coffeeshop.model.InfoBarSettings;

public interface InfoBarService {
    InfoBarSettings getSettings();
    void saveSettings(String message, boolean active);
}
