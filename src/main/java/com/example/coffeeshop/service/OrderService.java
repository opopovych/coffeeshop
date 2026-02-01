package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Order;
import java.util.List;

public interface OrderService {
    public Order createOrder(String name,String surName, String phone, String address, String payment, String comment);
    List<Order> findAll();
    Order findById(Long id);
    Order update(Order order);
    void deleteOrder(Long id);
    void updateStatus(Long orderId, String action);


    }
