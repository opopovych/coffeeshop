package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.OriginCountryService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/coffee")
public class CoffeeAdminController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;


    @Autowired
    private BrandService brandService;

    @Autowired
    private OriginCountryService originCountryService;

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

        return "redirect:/admin/coffee";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        coffeeBeanService.delete(id);
        return "redirect:/admin/coffee";
    }
}
