package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Brand;
import com.example.coffeeshop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
            @ModelAttribute Brand brand
    ) {
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
            @ModelAttribute Brand brand
    ) {
        brand.setId(id);
        brandService.save(brand);
        return "redirect:/admin/coffee/brands";
    }

    // -------------------- DELETE BRAND --------------------
    @GetMapping("/delete-brand/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.delete(id);
        return "redirect:/admin/coffee/brands";
    }

}
