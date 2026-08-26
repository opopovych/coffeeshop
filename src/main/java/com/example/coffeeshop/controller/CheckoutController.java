package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.DiscountService;
import com.example.coffeeshop.service.OrderService;
import com.example.coffeeshop.service.ShopSettingsService;
import com.example.coffeeshop.service.TelegramService;
import com.example.coffeeshop.service.impl.CartServiceImpl;
import com.example.coffeeshop.service.impl.PdfService;
import com.example.coffeeshop.service.impl.TelegramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/order/checkout")
public class CheckoutController {

    @Autowired
    private  CartServiceImpl cartService;
    @Autowired
    private  OrderService orderService;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private ShopSettingsService settingsService; // Сервіс налаштувань (ФОП, IBAN тощо)
    @Autowired
    private DiscountService discountService; // Сервіс знижок


    @GetMapping
    public String checkoutForm(Model model) {
        model.addAttribute("cart", cartService.getCart());

        // ВИПРАВЛЕНО: Викликаємо метод зі знижкою, який ми створили в CartService
        BigDecimal finalTotal = cartService.getTotalWithDiscount();

        model.addAttribute("total", finalTotal.doubleValue());
        return "checkout";
    }
    @PostMapping
    public String placeOrder(@RequestParam String name,
                             @RequestParam String surName,
                             @RequestParam String phone,
                             @RequestParam String cityName,
                             @RequestParam String wareHouse,
                             @RequestParam(required = false) String deliveryProvider,
                             @RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(required = false) String comment,
                             Model model) {

        // Визначаємо назву служби для красивого запису в адресу
        String providerName = (deliveryProvider != null && deliveryProvider.equals("UP")) ? "Укрпошта" : "Нова пошта";

        // Формат адреси
        String address = "Місто - " + cityName + ", " + providerName + ", " + wareHouse;

        // Створюємо замовлення
        Order order = orderService.createOrder(name, surName, phone, address, paymentMethod, comment);

        // 1. Надсилаємо текстове сповіщення в Telegram (як і раніше)
        //telegramService.sendOrderNotification(order);

        // 2. ДОДАЄМО: Генеруємо PDF та відправляємо файл у Telegram
        try {
            byte[] pdfData = pdfService.generatePdf(
                    order,
                    settingsService.getSettings(),
                    discountService.getSettings()
            );

            telegramService.sendPdfDocument(
                    pdfData,
                    name + "-" + surName + "_" + order.getId() + ".pdf",
                    order
            );
        } catch (Exception e) {
            System.err.println("❌ Помилка генерації або відправки PDF у Telegram: " + e.getMessage());
        }

        model.addAttribute("order", order);
        return "order-success";
    }

}