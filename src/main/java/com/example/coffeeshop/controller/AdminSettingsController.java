package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.impl.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/settings")
public class AdminSettingsController {
    
    @Autowired
    private DiscountService discountService;

    @PostMapping("/discount")
    public String updateDiscountSettings(@RequestParam Double threshold,
                                         @RequestParam Double discountPercent,
                                         @RequestParam(defaultValue = "false") boolean active, // Додаємо цей параметр
                                         RedirectAttributes redirectAttributes) {

        // Передаємо всі ТРИ параметри в сервіс
        discountService.saveSettings(threshold, discountPercent, active);

        redirectAttributes.addFlashAttribute("successMessage", "Налаштування знижки оновлено!");
        return "redirect:/admin/coffeeList";
    }

}