package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.service.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramServiceImpl implements TelegramService {

    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;
    private final String CHAT_ID = "585786382";

//    public void sendOrderNotification(Order order) {
//        StringBuilder message = new StringBuilder();
//        message.append("📦 *НОВЕ ЗАМОВЛЕННЯ IJO COFFEE*\n\n");
//        message.append("👤 *Клієнт:* ").append(order.getCustomerName()).append(" ").append(order.getSurName()).append("\n");
//        message.append("📞 *Тел:* ").append(order.getPhone()).append("\n");
//        message.append("📍 *Адреса:* ").append(order.getDeliveryAddress()).append("\n");
//
//// ДОДАЄМО СПОСІБ ОПЛАТИ
//        message.append("💳 *Оплата:* ").append(order.getPaymentMethod()).append("\n\n");
//
//        message.append("☕ *Товари:*\n");
//// Далі йде цикл по товарах...
//        // Припустимо, у вас є список позицій у замовленні
//        order.getItems().forEach(item -> {
//            message.append("• ").append(item.getProductFormat() + " -- ").append(item.getCoffeeBrand() + " " + item.getName())
//                    .append(" (").append(item.getQuantity()).append(" шт.)\n");
//        });
//
//        // Додаємо коментар, якщо він не порожній
//        if (order.getComment() != null && !order.getComment().isBlank()) {
//            message.append("💬 *Коментар:* ").append(order.getComment()).append("\n\n");
//        } else {
//            message.append("\n"); // Просто відступ, якщо коментаря немає
//        }
//
//        message.append("\n💰 *РАЗОМ ДО ОПЛАТИ: ").append(order.getTotalPrice()).append(" грн*");
//
//        sendRawMessage(message.toString());
//    }

@Override
    public void sendPdfDocument(byte[] pdfData, String fileName, Order order) {
        String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument";

        // Формуємо текст замовлення, який буде підписом до файлу
        StringBuilder message = new StringBuilder();
        message.append("📦 *НОВЕ ЗАМОВЛЕННЯ IJO COFFEE*\n\n");
        message.append("👤 *Клієнт:* ").append(order.getCustomerName()).append(" ").append(order.getSurName()).append("\n");
        message.append("📞 *Тел:* ").append(order.getPhone()).append("\n");
        message.append("📍 *Адреса:* ").append(order.getDeliveryAddress()).append("\n");
        message.append("💳 *Оплата:* ").append(order.getPaymentMethod()).append("\n\n");

        message.append("☕ *Товари:*\n");
        order.getItems().forEach(item -> {
            message.append("• ").append(item.getProductFormat() + " -- ").append(item.getCoffeeBrand() + " " + item.getName())
                    .append(" (").append(item.getQuantity()).append(" шт.)\n");
        });

        if (order.getComment() != null && !order.getComment().isBlank()) {
            message.append("💬 *Коментар:* ").append(order.getComment()).append("\n\n");
        } else {
            message.append("\n");
        }

        message.append("💰 *РАЗОМ ДО ОПЛАТИ: ").append(order.getTotalPrice()).append(" грн*");

        // Створюємо MultiValueMap для відправки файлу (multipart/form-data)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", CHAT_ID);
        body.add("caption", message.toString());
        body.add("parse_mode", "Markdown"); // Щоб стилізація зі звіроподібними зірочками (*bold*) працювала в підписі

        // Перетворюємо байти на файл для Telegram
        ByteArrayResource resource = new ByteArrayResource(pdfData) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };
        body.add("document", resource);

        // Відправляємо POST-запитом
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, body, String.class);
        System.out.println("✅ Накладну разом із повним описом замовлення успішно надіслано в Telegram!");
    }


//    @Override
//    public void sendRawMessage(String text) {
//        try {
//            // 1. Базовий URL без параметрів
//            String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
//
//            // 2. Створюємо карту параметрів
//            Map<String, String> params = new HashMap<>();
//            params.put("chat_id", CHAT_ID);
//            params.put("text", text);
//            params.put("parse_mode", "Markdown");
//
//            // 3. Відправляємо (RestTemplate сам закодує текст правильно)
//            RestTemplate restTemplate = new RestTemplate();
//            restTemplate.getForObject(url + "?chat_id={chat_id}&text={text}&parse_mode={parse_mode}",
//                    String.class, params);
//
//            System.out.println("✅ Замовлення успішно надіслано в Telegram!");
//        } catch (Exception e) {
//            System.err.println("❌ Помилка відображення: " + e.getMessage());
//        }
//    }

}