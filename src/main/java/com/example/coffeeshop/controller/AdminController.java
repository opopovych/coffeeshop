package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping()
    public String adminPanel(Model model) {
        model.addAttribute("ordersCount", dashboardService.getNewOrdersCount());
        model.addAttribute("productsCount", dashboardService.getProductsCount());
        model.addAttribute("brandsCount", dashboardService.getBrandsCount());

        return "admin/admin-panel"; // ім'я Thymeleaf файлу
    }
}
