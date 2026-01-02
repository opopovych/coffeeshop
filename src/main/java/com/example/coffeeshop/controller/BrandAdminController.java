package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Brand;
import com.example.coffeeshop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/admin/coffee")
public class BrandAdminController {
    @Autowired
    private BrandService brandService;

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
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdir();

        String filename = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
        FileOutputStream fos = new FileOutputStream(uploadDir + filename);
        fos.write(logo.getBytes());
        fos.close();
        brand.setPhotoPath(filename);
        brandService.save(brand);
        return "redirect:/admin/coffee/brands";
    }

    // -------------------- LIST ALL BRANDS --------------------
    @GetMapping("/brands")
    public String listBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "admin/brand-list";
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
            if (existing.getPhotoPath() != null) {
                File oldFile = new File("uploads/" + existing.getPhotoPath());
                if (oldFile.exists()) oldFile.delete();
            }

            // 2) зберігаємо нове фото
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdir();

            String newFilename = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            FileOutputStream fos = new FileOutputStream(uploadDir + newFilename);
            fos.write(logo.getBytes());
            fos.close();

            existing.setPhotoPath(newFilename);
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
