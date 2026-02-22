package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.DiscountService;
import com.example.coffeeshop.service.InfoBarService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final InfoBarService infoBarService;
    private final DiscountService discountService;

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        // Тепер ці об'єкти доступні автоматично у всіх HTML-файлах
        model.addAttribute("infoBarSettings", infoBarService.getSettings());
        model.addAttribute("discountSettings", discountService.getSettings());
    }
}