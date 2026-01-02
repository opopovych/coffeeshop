package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.impl.NovaPoshtaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipping")
public class ShippingController {

    @Autowired
    private NovaPoshtaService npService;

    @GetMapping("/cities")
    public List<Map<String, Object>> searchCities(@RequestParam String name) {
        return npService.getCities(name);
    }

    @GetMapping("/warehouses")
    public List<Map<String, Object>> getWarehouses(@RequestParam String cityRef) {
        return npService.getWarehouses(cityRef);
    }
}