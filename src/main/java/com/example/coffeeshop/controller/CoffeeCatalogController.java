package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.repository.BrandRepository;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import java.util.List;

import com.example.coffeeshop.service.DiscountService;
import com.example.coffeeshop.service.OriginCountryService;
import com.example.coffeeshop.service.impl.DiscountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private BrandRepository brandRepository;

    // SRP: Використовуємо @ModelAttribute, щоб фільтри були доступні всюди автоматично
    @ModelAttribute
    public void addFilterAttributes(Model model) {
        List<Brand>brands = brandRepository.findAllWithActiveProducts();
        model.addAttribute("brands", brands);
        model.addAttribute("countries", originCountryService.findAll());
        model.addAttribute("intensities", Intensity.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("compositions", Composition.values());
        model.addAttribute("acidityLevels", Acidity.values());
        model.addAttribute("productFormats", ProductFormat.values());

        // Додаємо налаштування знижки для всього контролера
        DiscountSettings settings = discountService.getSettings();
        model.addAttribute("discountSettings", (settings != null && settings.isActive()) ? settings : null);
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/coffee")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "9") int size,
                       @RequestParam(required = false) String query,
                       Model model,
                       HttpServletRequest request) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<CoffeeBean> coffeePage;

        if (query != null && !query.trim().isEmpty()) {
            coffeePage = coffeeBeanService.search(query, pageable);
        } else {
            coffeePage = coffeeBeanService.findAllActiveRandom(pageable);
        }

        // ВАЖЛИВО: Назва має бути coffeeList, як у вашому th:each
        model.addAttribute("coffeeList", coffeePage.getContent());
        model.addAttribute("coffeePage", coffeePage); // для пагінації

        // ... ваші SEO дані ...

        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return "catalog :: #coffee-container";
        }

        return populateCatalogModel(model, coffeePage, page);
    }
    @GetMapping("/coffee/filter")
    public String filter(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) ProductFormat format, // Змінено на Enum
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) List<Intensity> intensity,
            @RequestParam(required = false) List<RoastLevel> roast,
            @RequestParam(required = false) List<Bitterness> bitterness,
            @RequestParam(required = false) List<Composition> composition,
            @RequestParam(required = false) List<Acidity> acidity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        // Налаштування сортування
        Sort sorting = "priceAsc".equals(sort) ? Sort.by("price").ascending() :
                "priceDesc".equals(sort) ? Sort.by("price").descending() :
                "nameAsc".equals(sort) ? Sort.by("name").ascending() : Sort.unsorted();

        Pageable pageable = PageRequest.of(page, size, sorting);

        // Виклик сервісу з оновленим типом ProductFormat
        Page<CoffeeBean> coffeePage = coffeeBeanService.filter(
                format, brandId, countryId,
                clean(intensity), clean(roast), clean(bitterness),
                clean(composition), clean(acidity), pageable
        );

        // Логіка підбору доступних брендів та країн (для каскадних фільтрів)
        List<Brand> availableBrands = (countryId != null)
                ? brandService.getBrandsByCountry(countryId)
                : brandService.findAll();

        List<OriginCountry> countries = (brandId != null)
                ? originCountryService.getCountriesByBrand(brandId)
                : originCountryService.findAll();

        // Додавання назв для відображення вибраних фільтрів в UI
        if (brandId != null) {
            Brand b = brandService.findById(brandId);
            if (b != null) model.addAttribute("selectedBrandName", b.getName());
        }
        if (countryId != null) {
            OriginCountry c = originCountryService.findById(countryId);
            if (c != null) model.addAttribute("selectedCountryName", c.getName());
        }

        // Передаємо значення назад у модель, щоб зберегти стан фільтрів у UI
        model.addAttribute("format", format); // Використовуємо в селекті
        model.addAttribute("brandId", brandId);
        model.addAttribute("countryId", countryId);
        model.addAttribute("selectedIntensity", intensity);
        model.addAttribute("selectedRoast", roast);
        model.addAttribute("sort", sort);

        return populateCatalogModel(model, coffeePage, page);
    }

    @GetMapping("/coffee/{id}")
    public String details(
            @PathVariable Long id,
            @RequestParam(required = false) Integer page,
            Model model) {

        if (page != null) {
            return "redirect:/coffee/" + id;
        }

        CoffeeBean coffee = coffeeBeanService.findById(id);
        model.addAttribute("coffee", coffee);

        String seoTitle =
                "Кава " + coffee.getName() + " "
                        + coffee.getBrand().getName()
                        + " | IJO Coffee";

        model.addAttribute("seoTitle", seoTitle);

        String description = coffee.getDescription();

        if (description != null && description.length() > 155) {
            description = description.substring(0, 152) + "...";
        }

        model.addAttribute("seoDescription", description);

        return "coffee-details";
    }

    @GetMapping("/coffee/search")
    public String searchCoffee(@RequestParam("query") String query, Model model) {
        List<CoffeeBean> coffee = coffeeBeanService.search(query);
        model.addAttribute("coffeeList", coffee);
        model.addAttribute("query", query);
        return "catalog"; // твоя сторінка списку товарів
    }

    @GetMapping("/brandhistory")
    public String showHistoryPage() {
        return "brandhistory";
    }
    @GetMapping("/countries")
    public String showCountriesPage() {
        return "countrieshistory"; // Назва вашого HTML файлу
    }
    @GetMapping("/history")
    public String showHistory() {
        return "history"; //
    }

    @GetMapping("/api/countries-by-brand")
    @ResponseBody
    public List<OriginCountry> getCountriesByBrand(@RequestParam Long brandId) {
        // Використовуємо метод сервісу, який ми раніше обговорили
        return originCountryService.getCountriesByBrand(brandId);
    }
    @GetMapping("/api/all-countries")
    @ResponseBody
    public List<OriginCountry> getAllCountries() {
        return originCountryService.findAll();
    }
    @GetMapping("/api/brands-by-country")
    @ResponseBody
    public List<Brand> getBrandsByCountry(@RequestParam Long countryId) {
        return brandService.getBrandsByCountry(countryId);
    }

    @GetMapping("/api/all-brands")
    @ResponseBody
    public List<Brand> getAllBrands() {
        return brandService.findAll();
    }

    private String populateCatalogModel(Model model, Page<CoffeeBean> pageData, int page) {
        model.addAttribute("coffeeList", pageData.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageData.getTotalPages());
        return "catalog";
    }
    private <T> List<T> clean(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list;
    }






}
