package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Order;
import com.example.coffeeshop.model.Status;
import com.example.coffeeshop.model.dto.ProductSelectDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    long countOrdersByStatusNot(Status status);

    List<Order> findByStatus(Status status);

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        LEFT JOIN FETCH o.items
        ORDER BY o.id DESC
    """)
    List<Order> findAllWithItems();

    @Query("""
        SELECT DISTINCT o
        FROM Order o
        LEFT JOIN FETCH o.items
        WHERE o.status = :status
        ORDER BY o.id DESC
    """)
    List<Order> findByStatusWithItems(Status status);

}
