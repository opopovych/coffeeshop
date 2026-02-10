package com.example.coffeeshop.controller;

import com.example.coffeeshop.mapper.CoffeeBeanMapper;
import com.example.coffeeshop.model.*;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import com.example.coffeeshop.repository.BrandRepository;
import com.example.coffeeshop.repository.CoffeeBeanRepository;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import java.util.List;

import com.example.coffeeshop.service.OriginCountryService;
import com.example.coffeeshop.service.impl.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

@Controller
public class CoffeeCatalogController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private OriginCountryService originCountryService;
    @Autowired
    private DiscountService discountService;

    @GetMapping("/brandhistory")
    public String showHistoryPage(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brandhistory";
    }
    @GetMapping("/countries")
    public String showCountriesPage(Model model) {
        // Отримуємо список усіх країн з бази даних (де вже лежить наша історія)
        List<OriginCountry> countries = originCountryService.findAll();
        model.addAttribute("countries", countries);

        // Також передаємо бренди, якщо вони потрібні для футера або фільтрів
        model.addAttribute("brands", brandService.findAll());

        return "countrieshistory"; // Назва вашого HTML файлу
    }

    @GetMapping("/coffee")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model
    ) {
        // 1. Створюємо налаштування пагінації (12 товарів, сортування за ID)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 2. Отримуємо СТОРІНКУ активних товарів
        //Page<CoffeeBean> coffeePage = coffeeBeanService.findAllActive(pageable);
        // Замість findAllActive використовуй:
        Page<CoffeeBean> coffeePage = coffeeBeanService.findAllActiveRandom(pageable);

        // 3. Передаємо дані, які очікує ваш catalog.html
        DiscountSettings settings = discountService.getSettings();
        if (settings != null && settings.isActive()) {
            model.addAttribute("discountSettings", settings);
        } else {
            model.addAttribute("discountSettings", null);
        }
        model.addAttribute("coffeeList", coffeePage.getContent()); // Список товарів для поточної сторінки
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coffeePage.getTotalPages());

        // Додаємо дані для випадаючих списків фільтрів
        addFilterAttributes(model);

        return "catalog";
    }
    @GetMapping
    public String index(Model model) {
        // Беремо всі бренди з бази для каруселі
        model.addAttribute("brands", brandService.findAll());
        return "index"; // ваш файл index.html
    }
    @GetMapping("/history")
    public String showHistory(Model model) {
        //model.addAttribute("brands", brandService.findAll());
        return "history"; //
    }

    @GetMapping("/coffee/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("coffee", coffeeBeanService.findById(id));
        return "coffee-details";
    }
    @GetMapping("/coffee/search")
    public String searchCoffee(@RequestParam("query") String query, Model model) {
        List<CoffeeBean> coffee = coffeeBeanService.search(query);
        model.addAttribute("coffeeList", coffee);
        model.addAttribute("query", query);
        return "catalog"; // твоя сторінка списку товарів
    }
    @GetMapping("/coffee/filter")
    public String filter(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long countryId,
            @RequestParam(value = "intensity", required = false) List<Intensity> intensity,
            @RequestParam(value = "roast", required = false) List<RoastLevel> roast,
            @RequestParam(value = "bitterness", required = false) List<Bitterness> bitterness,
            @RequestParam(value = "composition", required = false) List<Composition> composition,
            @RequestParam(value = "acidity", required = false) List<Acidity> acidity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        addFilterAttributes(model);

        // Це критично важливий блок: якщо список порожній, робимо його null
        List<Intensity> intensityParam = (intensity == null || intensity.isEmpty()) ? null : intensity;
        List<RoastLevel> roastParam = (roast == null || roast.isEmpty()) ? null : roast;
        List<Bitterness> bitternessParam = (bitterness == null || bitterness.isEmpty()) ? null : bitterness;
        List<Composition> compositionParam = (composition == null || composition.isEmpty()) ? null : composition;
        List<Acidity> acidityParam = (acidity == null || acidity.isEmpty()) ? null : acidity;

        Sort sorting = Sort.unsorted();
        if ("priceAsc".equals(sort)) sorting = Sort.by("price").ascending();
        else if ("priceDesc".equals(sort)) sorting = Sort.by("price").descending();
        else if ("nameAsc".equals(sort)) sorting = Sort.by("name").ascending();

        Pageable pageable = PageRequest.of(page, size, sorting);

        // Передаємо в сервіс ТІЛЬКИ очищені параметри
        Page<CoffeeBean> coffeePage = coffeeBeanService.filter(
                brandId, countryId, intensityParam, roastParam,
                bitternessParam, compositionParam, acidityParam, pageable
        );
        DiscountSettings settings = discountService.getSettings();
        if (settings != null && settings.isActive()) {
            model.addAttribute("discountSettings", settings);
        } else {
            model.addAttribute("discountSettings", null);
        }

        model.addAttribute("coffeeList", coffeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coffeePage.getTotalPages());

        // Передаємо назад у модель оригінальні об'єкти (для роботи th:selected)
        model.addAttribute("brandId", brandId);
        model.addAttribute("countryId", countryId);
        model.addAttribute("intensity", intensity);
        model.addAttribute("roast", roast);
        model.addAttribute("bitterness", bitterness);
        model.addAttribute("composition", composition);
        model.addAttribute("acidity", acidity);
        model.addAttribute("sort", sort);

        return "catalog";
    }


    private void addFilterAttributes(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("countries",originCountryService.findAll());
        model.addAttribute("intensities", Intensity.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("compositions", Composition.values());
        model.addAttribute("acidityLevels", Acidity.values());
    }


}
