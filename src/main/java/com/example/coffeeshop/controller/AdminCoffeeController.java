package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.FileService;
import com.example.coffeeshop.service.OriginCountryService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/coffee")
public class AdminCoffeeController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OriginCountryService originCountryService;
    @Autowired
    private FileService fileService;

    // SRP: Один метод для всіх довідників. Spring автоматично додасть це у всі GET/POST запити.
    @ModelAttribute
    public void addFormAttributes(Model model) {
        model.addAttribute("countries", originCountryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("weights", ProductWeight.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("acidityLevels", Acidity.values());
        model.addAttribute("compositions", Composition.values());
        model.addAttribute("intensityLevels", Intensity.values());
    }


    // Головний список товарів
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("products", (search != null) ?
                coffeeBeanService.search(search) : coffeeBeanService.findAll());
        return "admin/product-list"; // Твій HTML файл
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProductApi(@PathVariable Long id) {
        CoffeeBean p = coffeeBeanService.findById(id);
        if (p == null) return ResponseEntity.notFound().build();

        Map<String, Object> res = new HashMap<>();
        res.put("name", p.getName());
        res.put("price", p.getPrice());
        res.put("photoPath", p.getPhotoPath());
        res.put("description", p.getDescription());

        // Згідно з вашими файлами Brand.java та OriginCountry.java
        res.put("brand", p.getBrand() != null ? p.getBrand().getName() : "-");
        res.put("country", p.getOriginCountry() != null ? p.getOriginCountry().getName() : "-");

        // Згідно з RoastLevel.java
        res.put("roast", p.getRoastLevel() != null ? p.getRoastLevel().getDisplayName() : "-");

        // Інші Enum (додайте аналогічно, якщо там є displayName)
        res.put("composition", p.getComposition() != null ? p.getComposition().getDisplayName() : "-");
        res.put("weight", p.getWeight() != null ? p.getWeight().getDisplayName() : "-");
        res.put("bitterness", p.getBitterness() != null ? p.getBitterness().getDisplayName() : "-");
        res.put("acidity", p.getAcidity() != null ? p.getAcidity().getDisplayName() : "-");
        res.put("intensity", p.getIntensity() != null ? p.getIntensity().getDisplayName() : "-");

        return ResponseEntity.ok(res);
    }

    // --- ОДИНОЧНЕ ОНОВЛЕННЯ (AJAX для перемикачів) ---
    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam Long id, @RequestParam String field, @RequestParam boolean value) {
        coffeeBeanService.updateSingleField(id, field, value); // Логіку switch перенесіть у сервіс
        return ResponseEntity.ok().build();
    }

    // --- МАСОВЕ ОНОВЛЕННЯ (AJAX) ---
    @PostMapping("/bulk-update")
    @ResponseBody
    public ResponseEntity<?> bulkUpdate(@RequestParam("ids[]") List<Long> ids,
                                        @RequestParam String field,
                                        @RequestParam boolean value) {
        List<CoffeeBean> products = coffeeBeanService.findAllById(ids);
        products.forEach(p -> {
            switch (field) {
                case "active": p.setActive(value); break;
                case "promo": p.setPromo(value); break;
                case "isHit": p.setHit(value); break;
            }
        });
        coffeeBeanService.saveAll(products);
        return ResponseEntity.ok().build();
    }

    // --- МАСОВЕ ВИДАЛЕННЯ (AJAX) ---
    @PostMapping("/bulk-delete")
    @ResponseBody
    public ResponseEntity<?> bulkDelete(@RequestParam("ids[]") List<Long> ids) {
        coffeeBeanService.deleteAllById(ids);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("coffeeBean", new CoffeeBean());
        return "admin/coffee-add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute CoffeeBean coffeeBean, // Використовуйте цей об'єкт
            @RequestParam("photo") MultipartFile photoFile
    ) throws IOException {
        coffeeBean.setPhotoPath(fileService.saveFile(photoFile));
        coffeeBeanService.save(coffeeBean);
        return "redirect:/admin/coffee/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("coffeeBean", coffeeBeanService.findById(id));
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
            fileService.deleteFile(existing.getPhotoPath());

            // 2) зберігаємо нове фото

            existing.setPhotoPath(fileService.saveFile(photoFile));
        }

        // --- ЗБЕРЕГТИ ---
        coffeeBeanService.save(existing);

        return "redirect:/admin/coffee/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        coffeeBeanService.delete(id);
        return "redirect:/admin/coffee/list";
    }
}
