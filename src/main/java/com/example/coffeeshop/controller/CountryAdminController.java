package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.OriginCountry;
import com.example.coffeeshop.service.OriginCountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/coffee")
public class CountryAdminController {

    @Autowired
    private OriginCountryService originCountryService;

    // -------------------- LIST ALL COUNTRIES --------------------
    @GetMapping("/country")
    public String listCountries(Model model) {
        model.addAttribute("countries", originCountryService.findAll());
        return "admin/country-list";
    }

    // -------------------- ADD COUNTRY --------------------
    @GetMapping("/add-country")
    public String addCountryForm(Model model) {
        model.addAttribute("country", new OriginCountry());
        return "admin/country-add";
    }

    @PostMapping("/add-country")
    public String addCountry(@ModelAttribute OriginCountry country) {
        originCountryService.save(country);
        return "redirect:/admin/coffee/country";
    }

    // -------------------- EDIT COUNTRY --------------------
    @GetMapping("/edit-country/{id}")
    public String editCountryForm(@PathVariable Long id, Model model) {
        model.addAttribute("country", originCountryService.findById(id));
        return "admin/country-edit";
    }

    @PostMapping("/edit-country/{id}")
    public String editCountry(
            @PathVariable Long id,
            @ModelAttribute OriginCountry country
    ) {
        country.setId(id);
        originCountryService.save(country);
        return "redirect:/admin/coffee/country";
    }

    // -------------------- DELETE COUNTRY --------------------
    @GetMapping("/delete-country/{id}")
    public String deleteCountry(@PathVariable Long id) {
        originCountryService.delete(id);
        return "redirect:/admin/coffee/country";
    }
}
