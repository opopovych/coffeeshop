package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.RoastLevel;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.BrandService;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.OriginCountryService;
import com.example.coffeeshop.service.impl.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping()
    public String adminPanel(Model model) {
        model.addAttribute("ordersCount", dashboardService.getNewOrdersCount());
        model.addAttribute("productsCount", dashboardService.getProductsCount());
        model.addAttribute("brandsCount", dashboardService.getBrandsCount());

        return "admin/admin-panel"; // ім'я Thymeleaf файлу
    }
    @GetMapping("/coffee/search")
    public String searchInAdmin(@RequestParam("query") String query, Model model) {
        // Використовуємо вже існуючий метод пошуку
        List<CoffeeBean> foundCoffee = coffeeBeanService.search(query);

        model.addAttribute("coffeeList", foundCoffee);
        model.addAttribute("query", query); // повертаємо запит у форму для зручності
        return "admin/admin-coffee-list"; // ваша назва файлу списку товарів в адмінці
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
    public String showAdminCoffeeList(

            @RequestParam(required = false) Long brandId, Model model) {
        List<CoffeeBean> list;

        if (brandId != null) {
            // Використовуємо ваш метод пошуку за брендом
            list = coffeeBeanService.findByBrandId(brandId);
        } else {
            // Якщо бренд не обрано — показуємо все
            list = coffeeBeanService.findAll();
        }

        model.addAttribute("coffeeList", list);
        model.addAttribute("brands", brandService.findAll()); // Потрібно для випадаючого списку
        model.addAttribute("selectedBrandId", brandId); // Щоб зберегти вибір у селекті
               return "admin/admin-coffee-list";
    }
    @GetMapping("/orders")
    public String viewOrders(@RequestParam(value = "status", required = false) Status status, Model model) {
        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStatusOrderByIdDesc(status);
        } else {
            orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }
        model.addAttribute("orders", orders);
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
