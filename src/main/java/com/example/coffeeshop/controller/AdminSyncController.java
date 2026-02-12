package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.ProductData;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.SyncService;
import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminSyncController {

    private final CoffeeBeanService coffeeService;
    private final SyncService excelService; // Твій сервіс для обробки файлів

    public AdminSyncController(CoffeeBeanService coffeeService, SyncService excelService) {
        this.coffeeService = coffeeService;
        this.excelService = excelService;
    }

    // Головна сторінка розділу синхронізації
    @GetMapping("/sync")
    public String syncPage(Model model) {
        return "admin/sync-panel";
    }

    // Ендпоїнт: Оновлення цін та наявності
    @PostMapping("/upload-price")
    public String uploadPrice(@RequestParam("file") MultipartFile file,
                              @RequestParam("percent") Double percent,
                              RedirectAttributes redirectAttributes) {
        try {
            excelService.syncWithPriceList(file, percent);
            redirectAttributes.addFlashAttribute("success", "Ціни та наявність успішно оновлені!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при завантаженні цін: " + e.getMessage());
        }
        return "redirect:/admin/sync";
    }

    // Ендпоїнт: Тільки наявність
    @PostMapping("/upload-available")
    public String uploadAvailable(@RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        try {
            excelService.syncStatusOnly(file);
            redirectAttributes.addFlashAttribute("success", "Статуси наявності оновлені!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка: " + e.getMessage());
        }
        return "redirect:/admin/sync";
    }

    // Ендпоїнт: Пошук відсутніх товарів
    @PostMapping("/missing-products")
    public String findMissing(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        // Логіка пошуку товарів, яких немає в БД
        Map<String, ProductData> missing = excelService.lookMissingProducts(file);
        model.addAttribute("missingProducts", missing);
        return "admin/sync-panel";
    }
}
