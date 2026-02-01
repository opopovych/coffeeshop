package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.impl.CartService;
import com.example.coffeeshop.service.impl.TelegramService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final TelegramService telegramService;

    public CheckoutController(CartService cartService, OrderService orderService, TelegramService telegramService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.telegramService = telegramService;
    }

    @GetMapping
    public String checkoutForm(Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        return "checkout";
    }

    @PostMapping
    public String placeOrder(@RequestParam String name,
                             @RequestParam String surName,
                             @RequestParam String phone,
                             @RequestParam String cityName,
                             @RequestParam String wareHouse,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(required = false) String comment,
                             Model model) {
        String address = "Місто - " + cityName+ ", Нова пошта, " + wareHouse;
        // 1. Створюємо замовлення в БД
        Order order = orderService.createOrder(name,surName, phone, address,paymentMethod, comment);

        // 2. ВІДПРАВЛЯЄМО СПОВІЩЕННЯ В ТЕЛЕГРАМ
        // Передаємо об'єкт замовлення, щоб дістати з нього склад та ціну
        telegramService.sendOrderNotification(order);

        model.addAttribute("order", order);
        return "order-success";
    }

}