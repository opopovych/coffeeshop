package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Order;
import java.util.List;

public interface OrderService {
    public Order createOrder(String name, String phone, String address);
    List<Order> findAll();
    Order findById(Long id);
    Order update(Order order);
    void deleteOrder(Long id);


    }
