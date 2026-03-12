package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.dto.ProductReportDto;

import java.util.List;

public interface OrderService {
    Order createOrder(String name,String surName, String phone, String address, String payment, String comment);
    List<Order> findAll();
    Order findById(Long id);
    Order update(Order order);
    void deleteOrder(Long id);
    void updateStatus(Long orderId, String action);
    List<ProductReportDto> getWarehousePickList();
    void updateItemQuantity(Long orderId, Long itemId, int newQuantity);
    void addItemToOrder(Long orderId, Long productId, int quantity);
    }
