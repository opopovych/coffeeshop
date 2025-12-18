package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.dto.Cart;
import com.example.coffeeshop.model.dto.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service
@SessionScope // Щоб зберігати стан кошика в сесії користувача
public class CartService {

    private final Cart cart = new Cart();

    public Cart getCart() {
        return cart;
    }

    public void addToCart(CartItem item) {
        cart.addItem(item);
    }

    public void removeFromCart(Long coffeeId) {
        cart.removeItem(coffeeId);
    }

    public double getTotal() {
        return cart.getTotal();
    }

    public void updateQuantity(Long coffeeId, Integer quantity) {
        if (quantity <= 0) {
            removeFromCart(coffeeId);
            return;
        }

        cart.getItems().stream()
                .filter(item -> item.getCoffeeId().equals(coffeeId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }
    public void clear() {
        cart.clear();
    }


}
