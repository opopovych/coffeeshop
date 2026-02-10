package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.OriginCountryService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.example.coffeeshop.service.SyncService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/coffee")
public class CoffeeAdminController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;


    @Autowired
    private BrandService brandService;

    @Autowired
    private OriginCountryService originCountryService;
    @Autowired
    private SyncService syncService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("coffeeList", coffeeBeanService.findAll());
        return "admin/coffee-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("coffeeBean", new CoffeeBean());

        // ДОДАТИ: Списки для вибірок (DropDowns)
        // Ці сервіси мають бути попередньо інжектовані у ваш контролер
        model.addAttribute("countries", originCountryService.findAll());
        model.addAttribute("brands", brandService.findAll());

        // ДОДАТИ: Списки значень Enum для радіокнопок/вибірок
        model.addAttribute("weights", ProductWeight.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("acidityLevels", Acidity.values());
        model.addAttribute("compositions", Composition.values());
        model.addAttribute("intensityLevels", Intensity.values());

        return "admin/coffee-add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute CoffeeBean coffeeBean, // Використовуйте цей об'єкт
            @RequestParam("photo") MultipartFile photoFile
    ) throws IOException {
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdir();

        String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
        FileOutputStream fos = new FileOutputStream(uploadDir + filename);
        fos.write(photoFile.getBytes());
        fos.close();
        coffeeBean.setPhotoPath(filename);
        coffeeBeanService.save(coffeeBean);
        return "redirect:/admin/coffeeList";
    }
    @PostMapping("/adjust-prices")
    public String adjustPrices(@RequestParam("percent") Double percent) {
        List<CoffeeBean> coffeeList = coffeeBeanService.findAll();

        for (CoffeeBean coffee : coffeeList) {
            if (coffee.getPrice() != null) {
                // Розраховуємо нову ціну
                double newPrice = coffee.getPrice() * (1 + (percent / 100));

                // Закруглюємо до цілого числа (Math.round повертає long, тому перетворюємо в Double)
                coffee.setPrice((double) Math.round(newPrice));
            }
        }
        coffeeBeanService.saveAll(coffeeList);
        return "redirect:/admin/coffeeList";
    }

    @PostMapping("/bulk-status")
    public String bulkStatus(
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam(value = "action", required = false) String action) {

        if (productIds != null && !productIds.isEmpty() && action != null) {

            if ("delete".equals(action)) {
                // Множинне видалення
                coffeeBeanService.deleteAllById(productIds);
            } else {
                // Активація або Деактивація
                List<CoffeeBean> products = coffeeBeanService.findAllById(productIds);
                boolean targetStatus = action.equals("activate");

                products.forEach(p -> {
                    p.setActive(targetStatus);
                    // Про всяк випадок закругляємо ціну при збереженні
                    if (p.getPrice() != null) {
                        p.setPrice((double) Math.round(p.getPrice()));
                    }
                });
                coffeeBeanService.saveAll(products);
            }
        }

        return "redirect:/admin/coffeeList";
    }

    @GetMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Long id) {
        CoffeeBean coffee = coffeeBeanService.findById(id);
        if (coffee != null) {
            coffee.setActive(!coffee.isActive());
            // Закругляємо ціну при будь-якому збереженні, щоб не було .0
            if (coffee.getPrice() != null) {
                coffee.setPrice((double) Math.round(coffee.getPrice()));
            }
            coffeeBeanService.save(coffee);
        }
        return "redirect:/admin/coffeeList";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        model.addAttribute("coffeeBean", coffeeBeanService.findById(id));

        model.addAttribute("countries", originCountryService.findAll());
        model.addAttribute("brands", brandService.findAll());

        model.addAttribute("weights", ProductWeight.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("acidityLevels", Acidity.values());
        model.addAttribute("compositions", Composition.values());
        model.addAttribute("intensityLevels", Intensity.values());

        return "admin/coffee-edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @ModelAttribute CoffeeBean coffeeBean,
            @RequestParam(value = "photo", required = false) MultipartFile photoFile
    ) throws IOException {

        CoffeeBean existing = coffeeBeanService.findById(id);

        // --- ОНОВЛЮЄМО ПОЛЯ ---
        existing.setName(coffeeBean.getName());
        existing.setSku(coffeeBean.getSku());
        existing.setDescription(coffeeBean.getDescription());
        existing.setPrice(coffeeBean.getPrice());
        existing.setBrand(coffeeBean.getBrand());
        existing.setWeight(coffeeBean.getWeight());
        existing.setOriginCountry(coffeeBean.getOriginCountry());
        existing.setRoastLevel(coffeeBean.getRoastLevel());
        existing.setBitterness(coffeeBean.getBitterness());
        existing.setAcidity(coffeeBean.getAcidity());
        existing.setComposition(coffeeBean.getComposition());
        existing.setIntensity(coffeeBean.getIntensity());

        // --- ОНОВЛЕННЯ ФОТО (якщо завантажили нове) ---
        if (photoFile != null && !photoFile.isEmpty()) {

            // 1) видаляємо старе фото
            if (existing.getPhotoPath() != null) {
                File oldFile = new File("uploads/" + existing.getPhotoPath());
                if (oldFile.exists()) oldFile.delete();
            }

            // 2) зберігаємо нове фото
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdir();

            String newFilename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            FileOutputStream fos = new FileOutputStream(uploadDir + newFilename);
            fos.write(photoFile.getBytes());
            fos.close();

            existing.setPhotoPath(newFilename);
        }

        // --- ЗБЕРЕГТИ ---
        coffeeBeanService.save(existing);

        return "redirect:/admin/coffeeList";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        coffeeBeanService.delete(id);
        return "redirect:/admin/coffee";
    }
    @PostMapping("/upload-price")
    public String uploadPrice(@RequestParam("file") MultipartFile file, Double percent, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, виберіть файл.");
            return "redirect:/admin/coffeeList";
        }

        try {
            // Отримуємо звіт від сервісу
            SyncReport report = syncService.syncWithPriceList(file,percent);

            String message = String.format("Синхронізація завершена! Оновлено: %d, Вимкнено: %d",
                    report.getUpdated(), report.getDeactivated());

            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при читанні файлу: " + e.getMessage());
        }

        return "redirect:/admin/coffeeList";
    }
    @PostMapping("/upload-available")
    public String uploadAvailable(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Будь ласка, виберіть файл.");
            return "redirect:/admin/coffeeList";
        }

        try {
            // Отримуємо звіт від сервісу
            SyncReport report = syncService.syncStatusOnly(file);

            String message = String.format("Синхронізація завершена! Оновлено: %d, Вимкнено: %d",
                    report.getUpdated(), report.getDeactivated());

            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка при читанні файлу: " + e.getMessage());
        }

        return "redirect:/admin/coffeeList";
    }

    @PostMapping("/missing-products")
    public String handleMissingProducts(@RequestParam("file") MultipartFile file,
                                        HttpSession session,
                                        RedirectAttributes ra) {
        if (file.isEmpty()) {
            ra.addFlashAttribute("error", "Файл не обрано!");
            return "redirect:/admin/coffeeList";
        }

        try {
            // Отримуємо мапу відсутніх товарів (SKU -> ProductData)
            Map<String, ProductData> missingProducts = syncService.lookMissingProducts(file);

            // Зберігаємо в сесію
            session.setAttribute("missingProductsMap", missingProducts);

            return "redirect:/admin/coffee/missing-products-view";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Помилка обробки: " + e.getMessage());
            return "redirect:/admin/coffeeList";
        }
    }

    @GetMapping("/missing-products-view")
    public String showMissingProducts(HttpSession session, Model model) {
        Map<String, ProductData> missing = (Map<String, ProductData>) session.getAttribute("missingProductsMap");

        if (missing == null || missing.isEmpty()) {
            model.addAttribute("message", "Нових товарів не знайдено (всі товари з файлу вже є в базі).");
        }

        model.addAttribute("missingProducts", missing);
        return "admin/missing-products-page"; // Назва нового HTML файлу
    }

}
