package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.repository.BrandRepository;
import com.example.coffeeshop.repository.CoffeeBeanRepository;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired private CoffeeBeanRepository productRepository;
    @Autowired private BrandRepository brandRepository;

    @Override
    public long getNewOrdersCount() {
        List<Order> orders = orderRepository.findAll();

        // Проходимо по списку та виправляємо статус null
        orders.forEach(order -> {
            if (order.getStatus() == null) {
                order.setStatus(Status.WAITING); // або WHAITING, як у тебе в Enum
                orderRepository.save(order); // Зберігаємо зміни в БД
            }
        });

        // Тепер рахуємо всі, що не виконані
        return orders.stream()
                .filter(order -> order.getStatus() != Status.DONE)
                .count();
    }
    @Override
    public long getProductsCount() {
        return productRepository.count();
    }
    @Override
    public long getBrandsCount() {
        return brandRepository.count();
    }
}