package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.ShopSettings;
import com.example.coffeeshop.repository.ShopSettingsRepository;
import com.example.coffeeshop.service.ShopSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopSettingsServiceImpl implements ShopSettingsService {

    private final ShopSettingsRepository repository;

    @Override
    public ShopSettings getSettings() {
        // Шукаємо налаштування з ID 1, якщо немає — створюємо дефолтні
        return repository.findById(1L).orElseGet(() -> {
            ShopSettings defaultSettings = new ShopSettings();
            defaultSettings.setId(1L);
            defaultSettings.setVendorName("ФОП Попович О.І.");
            defaultSettings.setTaxNumber("0000000000");
            defaultSettings.setPhoneNumber("+380 00 000 00 00");
            defaultSettings.setShopAddress("Група 2");
            return repository.save(defaultSettings);
        });
    }

    @Override
    public void saveSettings(ShopSettings settings) {
        settings.setId(1L); // Гарантуємо, що ми завжди оновлюємо той самий запис
        repository.save(settings);
    }
}