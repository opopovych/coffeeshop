package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.DiscountSettings;
import com.example.coffeeshop.model.dto.Cart;
import com.example.coffeeshop.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository repository;

    public Double calculateTotal(Cart cart) {
        Double subtotal = cart.getTotal(); // Початкова сума всіх товарів
        DiscountSettings settings = repository.findById(1L).orElse(new DiscountSettings());

        if (subtotal >= settings.getThreshold()) {
            double multiplier = 1 - (settings.getDiscountPercent() / 100);
            return (double) Math.round(subtotal * multiplier);
        }
        return subtotal;
    }
    
    public DiscountSettings getSettings() {
        return repository.findById(1L).orElse(new DiscountSettings());
    }

    public void saveSettings(Double threshold, Double percent, boolean active) {
        DiscountSettings s = getSettings();
        s.setThreshold(threshold);
        s.setDiscountPercent(percent);
        s.setActive(active);
        repository.save(s);
    }
}