package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.InfoBarSettings;
import com.example.coffeeshop.repository.InfoBarRepository;
import com.example.coffeeshop.service.InfoBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoBarServiceImpl implements InfoBarService {
    @Autowired
    private InfoBarRepository repository;

    public InfoBarSettings getSettings() {
        return repository.findById(1L).orElse(new InfoBarSettings());
    }

    public void saveSettings(String message, boolean active) {
        InfoBarSettings settings = getSettings();
        settings.setMessage(message);
        settings.setActive(active);
        repository.save(settings);
    }
}
