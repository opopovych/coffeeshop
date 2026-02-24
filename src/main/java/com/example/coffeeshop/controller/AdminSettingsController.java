package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.ShopSettings;
import com.example.coffeeshop.service.ShopSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/settings")
@RequiredArgsConstructor
public class AdminSettingsController {

    private final ShopSettingsService settingsService;

    @GetMapping
    public String editSettings(Model model) {
        model.addAttribute("settings", settingsService.getSettings());
        return "admin/shop-settings"; // шлях до твоєї сторінки в templates
    }

    @PostMapping("/save")
    public String saveSettings(@ModelAttribute ShopSettings settings) {
        settingsService.saveSettings(settings);
        return "redirect:/admin/settings?success";
    }
}