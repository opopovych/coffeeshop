package com.example.coffeeshop.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        for (CartItem existing : items) {
            if (existing.getCoffeeId().equals(item.getCoffeeId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(Long coffeeId) {
        items.removeIf(i -> i.getCoffeeId().equals(coffeeId));
    }

    public double getTotal() {
        return items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }
    public void clear() {
        items.clear();
    }
}
