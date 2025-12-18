package com.example.coffeeshop.controller;

import com.example.coffeeshop.mapper.CoffeeBeanMapper;
import com.example.coffeeshop.model.*;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import java.util.List;
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
@RequestMapping("/coffee")
public class CoffeeCatalogController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;
    @Autowired
    private BrandService brandService;


    @GetMapping
    public String list(Model model) {
        model.addAttribute("coffeeList", coffeeBeanService.findAll());
        addFilterAttributes(model);

        return "catalog";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("coffee", coffeeBeanService.findById(id));
        return "coffee-details";
    }
    @GetMapping("/search")
    public String searchCoffee(@RequestParam("query") String query, Model model) {
        List<CoffeeBean> coffee = coffeeBeanService.search(query);
        model.addAttribute("coffeeList", coffee);
        model.addAttribute("query", query);
        return "catalog"; // твоя сторінка списку товарів
    }
    @GetMapping("/filter")
    public String filter(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Intensity intensity,
            @RequestParam(required = false) RoastLevel roast,
            @RequestParam(required = false) Bitterness bitterness,
            @RequestParam(required = false) Composition composition,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "9") int size,
            @RequestParam(required = false) String sort,
            Model model
    ) {
        addFilterAttributes(model);

        Sort sorting = Sort.unsorted();

        if ("priceAsc".equals(sort)) {
            sorting = Sort.by("price").ascending();
        } else if ("priceDesc".equals(sort)) {
            sorting = Sort.by("price").descending();
        } else if ("nameAsc".equals(sort)) {
            sorting = Sort.by("name").ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<CoffeeBean> coffeePage = coffeeBeanService.filter(
                brandId, intensity, roast, bitterness, composition, pageable
        );

        model.addAttribute("coffeeList", coffeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coffeePage.getTotalPages());

        model.addAttribute("brandId", brandId);
        model.addAttribute("intensity", intensity);
        model.addAttribute("roast", roast);
        model.addAttribute("bitterness", bitterness);
        model.addAttribute("composition", composition);
        model.addAttribute("sort", sort);

        return "catalog";
    }


    private void addFilterAttributes(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("intensities", Intensity.values());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("bitternessLevels", Bitterness.values());
        model.addAttribute("compositions", Composition.values());
    }


}
