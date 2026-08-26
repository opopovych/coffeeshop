package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.DiscountSettings;
import com.example.coffeeshop.model.dto.Cart;
import com.example.coffeeshop.model.dto.CartItem;
import com.example.coffeeshop.service.CartService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@SessionScope // Щоб зберігати стан кошика в сесії користувача
public class CartServiceImpl implements CartService {
    @Autowired
    private DiscountServiceImpl discountService;
    @Getter
    private final Cart cart = new Cart();

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
    public long getCartItemCount(){
        return cart.getItems().size();
    }
    public BigDecimal getTotalWithDiscount() {
        BigDecimal subtotal = BigDecimal.valueOf(getTotal()); // або getTotal() вже повертає BigDecimal
        DiscountSettings settings = discountService.getSettings();

        if (settings != null && settings.isActive() && subtotal.doubleValue() >= settings.getThreshold()) {
            BigDecimal discountMultiplier = BigDecimal.valueOf(1)
                    .subtract(BigDecimal.valueOf(settings.getDiscountPercent()).divide(BigDecimal.valueOf(100)));
            return subtotal.multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);
        }
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

}
