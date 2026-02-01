package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.OrderItem;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.OrderService;
import java.security.PublicKey;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(String name,String surName, String phone, String address, String payment, String comment) {
        Order order = new Order();
        order.setCustomerName(name);
        order.setSurName(surName);
        order.setPhone(phone);
        order.setDeliveryAddress(address);
        order.setTotalPrice(cartService.getTotal());
        order.setPaymentMethod(payment);
        order.setComment(comment);

        cartService.getCart().getItems().forEach(item -> {
            OrderItem oi = new OrderItem();
            oi.setCoffeeId(item.getCoffeeId());
            oi.setCoffeeBrand(item.getBrand());
            oi.setName(item.getName());
            oi.setPrice(item.getPrice());
            oi.setQuantity(item.getQuantity());
            oi.setOrder(order);
            order.getItems().add(oi);
        });

        Order saved = orderRepository.save(order);
        cartService.clear(); // очищення кошика

        return saved;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    public Order update(Order order) {
        return orderRepository.save(order);
    }

    // У вашому OrderService (або його імплементації OrderServiceImpl)
    public void updateStatus(Long orderId, String action) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Замовлення не знайдено: " + orderId));

        switch (action) {
            case "confirm" -> order.setStatus(Status.AT_WORK); // ✅ Прийняти -> В роботі
            case "done"    -> order.setStatus(Status.DONE);    // 📦 Виконано -> Виконано
            case "cancel"  -> {
                // Тут можна або видалити, або додати статус CANCELED в Enum
                orderRepository.delete(order);
                return;
            }
        }
        orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
