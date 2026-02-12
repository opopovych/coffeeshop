package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.service.CoffeeBeanService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/coffee")
public class AdminCoffeeController {

    private final CoffeeBeanService coffeeService;

    public AdminCoffeeController(CoffeeBeanService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @GetMapping("/list")
    public String listProducts(@RequestParam(required = false) String search, Model model) {
        List<CoffeeBean> products = (search != null) ?
            coffeeService.search(search) : coffeeService.findAll();
        model.addAttribute("products", products);
        return "admin/product-list";
    }

    // Швидке оновлення статусу (AJAX)
    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam Long id,
                                          @RequestParam String field,
                                          @RequestParam boolean value) {
        coffeeService.updateSingleField(id, field, value);
        return ResponseEntity.ok().build();
    }
}