package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.InfoBarService;
import com.example.coffeeshop.service.impl.DiscountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CoffeeBeanService coffeeBeanService;
    @Autowired
    private DiscountServiceImpl discountService;
    @Autowired
    private InfoBarService infoBarService;

    @GetMapping
    public String marketingPage(Model model) {
        model.addAttribute("discountSettings", discountService.getSettings());
        model.addAttribute("infoBarSettings", infoBarService.getSettings());
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

    @PostMapping("/adjust-prices")
    public String adjustPrices(@RequestParam("percent") Double percent, RedirectAttributes ra) {
        coffeeBeanService.adjustAllPrices(percent);
        ra.addFlashAttribute("success", "Ціни змінено на " + percent + "%");
        return "redirect:/admin/marketing";
    }

    @PostMapping("/settings/infobar")
    public String updateInfoBar(@RequestParam String message,
                                @RequestParam(required = false) boolean active,
                                RedirectAttributes ra) {
        infoBarService.saveSettings(message, active);
        ra.addFlashAttribute("success", "Інфо-стрічку оновлено!");
        return "redirect:/admin/marketing";
    }
}