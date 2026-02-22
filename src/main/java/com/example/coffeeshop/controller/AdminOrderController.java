package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.OrderService;
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

    @GetMapping("/orders")
    public String viewOrders(@RequestParam(value = "status", required = false) Status status, Model model) {
        List<Order> orders = (status != null)
                ? orderRepository.findByStatusOrderByIdDesc(status)
                : orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("orders", orders);
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
        model.addAttribute("order", order);
        return "admin/invoice-print";
    }
}
