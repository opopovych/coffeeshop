package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Order;

public interface TelegramService {
    //void sendOrderNotification(Order order);
    //void sendRawMessage(String text);
    void sendPdfDocument(byte[] pdfData, String fileName, Order order);
}
