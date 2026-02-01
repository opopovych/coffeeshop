package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countOrdersByStatusNot(Status status);
    List<Order> findByStatus(Status status);
    // Сортуємо за ID у зворотньому порядку (Desc)
    List<Order> findAllByOrderByIdDesc();

    // Якщо фільтруємо за статусом, теж додаємо сортування
    List<Order> findByStatusOrderByIdDesc(Status status);
}
