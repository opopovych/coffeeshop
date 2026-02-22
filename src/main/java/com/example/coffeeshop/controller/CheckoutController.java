package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.TelegramService;
import com.example.coffeeshop.service.impl.CartServiceImpl;
import com.example.coffeeshop.service.impl.TelegramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order/checkout")
public class CheckoutController {

    @Autowired
    private  CartServiceImpl cartService;
    @Autowired
    private  OrderService orderService;
    @Autowired
    private TelegramService telegramService;


    @GetMapping
    public String checkoutForm(Model model) {
        model.addAttribute("cart", cartService.getCart());

        // ВИПРАВЛЕНО: Викликаємо метод зі знижкою, який ми створили в CartService
        double finalTotal = cartService.getTotalWithDiscount();

        model.addAttribute("total", Math.round(finalTotal));
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