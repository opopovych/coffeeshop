package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.OrderItem;
import com.example.coffeeshop.model.ShopSettings;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository; // Краще теж замінити на сервіс у майбутньому
    @Autowired
    private ShopSettingsService settingsService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private CoffeeBeanService coffeeBeanService;

    @GetMapping("/orders")
    public String viewOrders(@RequestParam(value = "status", required = false) Status status, Model model) {
        List<Order> orders = (status != null)
                ? orderRepository.findByStatusOrderByIdDesc(status)
                : orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("orders", orders);
        model.addAttribute("allProducts", coffeeBeanService.findAll());
        return "admin/admin-orders";
    }

    @GetMapping("/edit/{id}")
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
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }
    @GetMapping("/orders/{id}/print")
    public String printOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        // Отримуємо налаштування (якщо немає - створюємо дефолтні)
        model.addAttribute("order", order);
        model.addAttribute("settings", settingsService.getSettings());
        model.addAttribute("discount", discountService.getSettings());
        return "admin/invoice-print";
    }
    // Оновлення кількості через AJAX
    @PostMapping("/orders/update-item-ajax")
    @ResponseBody
    public Double updateItemAjax(@RequestParam Long orderId, @RequestParam Long itemId, @RequestParam int quantity) {
        orderService.updateItemQuantity(orderId, itemId, quantity);
        return orderService.findById(orderId).getTotalPrice();
    }

    // Додавання товару через AJAX
    @PostMapping("/orders/add-item-ajax")
    @ResponseBody
    public OrderItem addItemAjax(@RequestParam Long orderId, @RequestParam Long productId, @RequestParam int quantity) {
        orderService.addItemToOrder(orderId, productId, quantity);
        // Отримуємо останній доданий елемент, щоб відправити його в JS
        Order order = orderService.findById(orderId);
        return order.getItems().get(order.getItems().size() - 1);
    }
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "orderId", required = false) Long orderId, Model model) {
        model.addAttribute("products", coffeeBeanService.findAll());
        model.addAttribute("orderId", orderId); // Передаємо ID замовлення в HTML
        return "admin/products-list";
    }
    @GetMapping("/orders/get-total/{id}")
    @ResponseBody
    public Double getTotal(@PathVariable Long id) {
        return orderService.findById(id).getTotalPrice();
    }
}
