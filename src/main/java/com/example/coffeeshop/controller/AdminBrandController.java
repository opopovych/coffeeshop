package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Brand;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/admin/coffee")
public class AdminBrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private FileService fileService;

    // -------------------- LIST ALL BRANDS --------------------
    @GetMapping("/brands")
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "admin/brand-list";
    }

    // -------------------- ADD BRAND --------------------
    @GetMapping("/add-brand")
    public String addBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "/admin/brand-add";
    }

    @PostMapping("/add-brand")
    public String addBrand(
            @ModelAttribute Brand brand,
            @RequestParam("logo") MultipartFile logo
    ) throws IOException {
        // SRP: Контролер не знає, ЯК зберігається файл, він просто просить це зробити
        brand.setPhotoPath(fileService.saveFile(logo));
        brandService.save(brand);
        return "redirect:/admin/coffee/brands";
    }

    // -------------------- EDIT BRAND --------------------
    @GetMapping("/edit-brand/{id}")
    public String editBrandForm(@PathVariable Long id, Model model) {
        model.addAttribute("brand", brandService.findById(id));
        return "admin/brand-edit";
    }

    @PostMapping("/edit-brand/{id}")
    public String editBrand(
            @PathVariable Long id,
            @ModelAttribute Brand brand,
            @RequestParam(value = "logo", required = false) MultipartFile logo
    ) throws IOException {
        Brand existing = brandService.findById(id);
        existing.setName(brand.getName());
        existing.setHistory(brand.getHistory());
        // --- ОНОВЛЕННЯ ФОТО (якщо завантажили нове) ---
        if (logo != null && !logo.isEmpty()) {

            // 1) видаляємо старе фото
            fileService.deleteFile(existing.getPhotoPath());

            // 2) зберігаємо нове фото
            existing.setPhotoPath(fileService.saveFile(logo));
        }
        brandService.save(existing);
        return "redirect:/admin/coffee/brands";
    }

    // -------------------- DELETE BRAND --------------------
    @GetMapping("/delete-brand/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.delete(id);
        return "redirect:/admin/coffee/brands";
    }

}
