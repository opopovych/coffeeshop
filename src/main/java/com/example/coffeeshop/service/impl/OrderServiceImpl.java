package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.OrderItem;
import com.example.coffeeshop.model.ProductWeight;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.model.dto.ProductReportDto;
import com.example.coffeeshop.repository.OrderRepository;
import com.example.coffeeshop.service.OrderService;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartServiceImpl cartService;

    public OrderServiceImpl(OrderRepository orderRepository, CartServiceImpl cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order createOrder(String name,String surName, String phone, String address, String payment, String comment) {
        Order order = new Order();
        order.setCustomerName(name);
        order.setSurName(surName);
        order.setPhone(phone);
        order.setDeliveryAddress(address);
        order.setTotalPrice(cartService.getTotalWithDiscount());
        order.setPaymentMethod(payment);
        order.setComment(comment);

        cartService.getCart().getItems().forEach(item -> {
            OrderItem oi = new OrderItem();
            oi.setCoffeeId(item.getCoffeeId());
            oi.setCoffeeBrand(item.getBrand());
            oi.setName(item.getName());
            oi.setPrice(item.getPrice());
            oi.setQuantity(item.getQuantity());
            oi.setWeight(item.getWeight());
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

    @Override
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
    public List<ProductReportDto> getWarehousePickList() {
        // 1. Беремо всі замовлення
        List<Order> allOrders = orderRepository.findAll();

        // 2. Map для групування (Ключ = Бренд + Назва + Вага)
        Map<String, ProductReportDto> pickListMap = new HashMap<>();

        for (Order order : allOrders) {
            // 3. Фільтруємо за статусами: тільки "В роботі" та "Очікує"
            if (order.getStatus() == Status.AT_WORK || order.getStatus() == Status.WAITING) {

                for (OrderItem item : order.getItems()) {
                    String key = item.getCoffeeBrand() + "|" + item.getName() + "|" + item.getWeight();

                    if (pickListMap.containsKey(key)) {
                        ProductReportDto existing = pickListMap.get(key);
                        existing.setTotalQuantity(existing.getTotalQuantity() + item.getQuantity());
                    } else {
                        pickListMap.put(key, new ProductReportDto(
                                item.getCoffeeBrand(),
                                item.getName(),
                                item.getWeight(),
                                (long) item.getQuantity()
                        ));
                    }
                }
            }
        }

        // 4. Сортуємо за брендом, щоб на складі було зручніше шукати
        List<ProductReportDto> result = new ArrayList<>(pickListMap.values());
        result.sort(Comparator.comparing(ProductReportDto::getBrand));

        return result;
    }
}
