package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.impl.CartService;
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

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String checkoutForm(Model model) {
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        return "checkout";
    }

    @PostMapping
    public String placeOrder(@RequestParam String name,
                             @RequestParam String phone,
                             @RequestParam String address,
                             Model model) {

        Order order = orderService.createOrder(name, phone, address);

        model.addAttribute("order", order);
        return "order-success";
    }

}
