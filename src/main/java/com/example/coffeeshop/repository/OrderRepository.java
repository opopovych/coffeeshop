package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countOrdersByStatusNot(Status status);
}
