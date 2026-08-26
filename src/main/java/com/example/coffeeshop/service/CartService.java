package com.example.coffeeshop.service;

import com.example.coffeeshop.model.dto.CartItem;

import java.math.BigDecimal;

public interface CartService {
    void addToCart(CartItem item);
    void removeFromCart(Long coffeeId);
    double getTotal();
    void updateQuantity(Long coffeeId, Integer quantity);
    void clear();
    long getCartItemCount();
    BigDecimal getTotalWithDiscount();
}
