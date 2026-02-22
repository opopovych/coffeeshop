package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.ProductData;
import com.example.coffeeshop.model.SyncReport;
import com.example.coffeeshop.service.SyncService;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sync")
public class AdminSyncController {
    @Autowired
    private SyncService syncService;

    @GetMapping
    public String syncPage() {
        return "admin/sync-panel";
    }
    @PostMapping("/upload-price")
    public String uploadPrice(@RequestParam("file") MultipartFile file,
                              @RequestParam(value = "percent", defaultValue = "0.0") Double percent,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Файл не обрано. Будь ласка, завантажте Excel-файл.");
            return "redirect:/admin/coffee/list";
        }

        try {
            // Викликаємо сервіс для синхронізації цін з націнкою
            SyncReport report = syncService.syncWithPriceList(file, percent);

            String message = String.format("Ціни успішно оновлено! Оновлено товарів: %d, Деактивовано (відсутні в прайсі): %d. Націнка: %.1f%%",
                    report.getUpdated(), report.getDeactivated(), percent);

            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            // Додаємо вивід помилки в консоль для дебагу
            redirectAttributes.addFlashAttribute("error", "Помилка обробки прайс-листа: " + e.getMessage());
        }

        return "redirect:/admin/sync";
    }

    @PostMapping("/upload-available")
    public String uploadAvailable(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, виберіть файл.");
            // ПЕРЕВІРТЕ ЦЕЙ ШЛЯХ: у вас він був /admin/coffeeList або /admin/coffee/list
            return "redirect:/admin/sync";
        }

        try {
            SyncReport report = syncService.syncStatusOnly(file);
            String message = String.format("Синхронізація статусів завершена! Оновлено: %d, Деактивовано: %d",
                    report.getUpdated(), report.getDeactivated());

            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка обробки файлу: " + e.getMessage());
        }

        return "redirect:/admin/sync";
    }

    // Метод для пошуку відсутніх товарів
    @PostMapping("/missing-products")
    public String handleMissingProducts(@RequestParam("file") MultipartFile file,
                                        HttpSession session,
                                        RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Файл не обрано!");
            return "redirect:/admin/sync";
        }

        try {
            // Отримуємо мапу відсутніх товарів
            Map<String, ProductData> missingProducts = syncService.lookMissingProducts(file);

            // Зберігаємо в сесію під чітким ім'ям
            session.setAttribute("missingProductsMap", missingProducts);

            // Редірект на сторінку відображення (відносний шлях у цьому ж контролері)
            return "redirect:/admin/sync/missing-products-view";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Помилка обробки: " + e.getMessage());
            return "redirect:/admin/sync";
        }
    }

    // Сторінка відображення результатів
    @GetMapping("/missing-products-view")
    public String showMissingProducts(HttpSession session, Model model) {
        Map<String, ProductData> missing = (Map<String, ProductData>) session.getAttribute("missingProductsMap");

        if (missing == null || missing.isEmpty()) {
            model.addAttribute("message", "Нових товарів не знайдено.");
        }

        model.addAttribute("missingProducts", missing);
        return "admin/missing-products-page";
    }
}
