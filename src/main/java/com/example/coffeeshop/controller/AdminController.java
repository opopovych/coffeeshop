package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.OriginCountryService;
import com.example.coffeeshop.service.impl.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CoffeeBeanService coffeeBeanService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private OriginCountryService originCountryService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private DashboardService dashboardService;

    @GetMapping()
    public String adminPanel(Model model) {
        model.addAttribute("ordersCount", dashboardService.getNewOrdersCount());
        model.addAttribute("productsCount", dashboardService.getProductsCount());
        model.addAttribute("brandsCount", dashboardService.getBrandsCount());

        return "admin/admin-panel"; // ім'я Thymeleaf файлу
    }
    @GetMapping("/brands")
    public String showAdminBrands(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "admin/admin-brands";
    }
    @GetMapping("/countries")
    public String showAdminCountries(Model model) {
        model.addAttribute("countries", originCountryService.findAll());
        return "admin/admin-countries";
    }
    @GetMapping("/coffeeList")
    public String showAdminCoffeeList(Model model) {
        model.addAttribute("coffeeList", coffeeBeanService.findAll());
        return "admin/admin-coffee-list";
    }
    @GetMapping("/orders")
    public String showAdminOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/admin-orders";
    }

    @GetMapping("/edit-order/{id}")
    public String editOrderForm(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "admin/order-edit";
    }

    @PostMapping("/edit-order/{id}")
    public String updateOrderStatus(
            @PathVariable Long id,
            @ModelAttribute Order order
    ) {
        Order existingOrder = orderService.findById(id);
        existingOrder.setStatus(order.getStatus()); // лише оновлюємо статус
        orderService.update(existingOrder);
        return "redirect:/admin/orders";
    }
    @GetMapping("/delete-order/{id}")
    public String deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

}
