package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.SyncReport;
import com.example.coffeeshop.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/sync")
public class SyncController {

    @Autowired
    private SyncService syncService;

    @PostMapping("/import-new")
    public String importNewProducts(@RequestParam("file") MultipartFile file, 
                                    RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, виберіть Excel-файл.");
            return "redirect:/admin/products"; // або твоя сторінка адмінки
        }

        try {
            // Викликаємо наш новий метод
            SyncReport report = syncService.importNewProducts(file);
            
            // Передаємо гарне повідомлення про результат в адмінку
            // ПРАВИЛЬНИЙ ВАРІАНТ
            String successMessage = String.format(
                    "Імпорт завершено! Додано нових товарів: %d. Пропущено (вже були в базі): %d.",
                    report.getUpdated(), report.getDeactivated()
            );
            redirectAttributes.addFlashAttribute("success", successMessage);
            
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Помилка під час читання файлу: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Сталася помилка: " + e.getMessage());
        }

        return "redirect:/admin/coffee/list";
    }
}