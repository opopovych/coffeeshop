package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.impl.DiscountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/marketing")
public class AdminMarketingController {

    private final CoffeeBeanService coffeeBeanService;
    private final DiscountService discountService;

    public AdminMarketingController(CoffeeBeanService coffeeBeanService, DiscountService discountService) {
        this.coffeeBeanService = coffeeBeanService;
        this.discountService = discountService;
    }

    @GetMapping
    public String marketingPage(Model model) {
        model.addAttribute("discountSettings", discountService.getSettings());
        return "admin/marketing-panel";
    }

    @PostMapping("/settings/discount")
    public String updateDiscount(@RequestParam Double threshold,
                                 @RequestParam Double discountPercent,
                                 @RequestParam(required = false) boolean active,
                                 RedirectAttributes redirectAttributes) {
        discountService.saveSettings(threshold, discountPercent, active);
        redirectAttributes.addFlashAttribute("success", "Налаштування знижок успішно збережено!");
        return "redirect:/admin/marketing";
    }

    // Метод adjustPrices залишаємо тут же, як обговорювали вище
}