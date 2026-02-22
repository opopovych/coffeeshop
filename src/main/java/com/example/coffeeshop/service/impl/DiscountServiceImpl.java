package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.DiscountSettings;
import com.example.coffeeshop.repository.DiscountRepository;
import com.example.coffeeshop.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountServiceImpl implements DiscountService {
    @Autowired
    private DiscountRepository repository;

    @Override
    public DiscountSettings getSettings() {
        return repository.findById(1L).orElse(new DiscountSettings());
    }

    @Override
    public void saveSettings(Double threshold, Double percent, boolean active) {
        DiscountSettings s = getSettings();
        s.setThreshold(threshold);
        s.setDiscountPercent(percent);
        s.setActive(active);
        repository.save(s);
    }

}