package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.DiscountSettings;
import com.example.coffeeshop.model.dto.Cart;
import com.example.coffeeshop.model.dto.CartItem;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.DiscountService;

import com.example.coffeeshop.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private  CoffeeBeanService coffeeBeanService;
    @Autowired
    private CartServiceImpl cartService;
    @Autowired
    private  DiscountService discountService;

    @PostMapping("/add/{coffeeId}")
    public String addToCart(@PathVariable Long coffeeId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        CoffeeBean coffee = coffeeBeanService.findById(coffeeId);

        CartItem item = new CartItem()
                .setCoffeeId(coffee.getId())
                .setName(coffee.getName())
                .setBrand(coffee.getBrand() != null ? coffee.getBrand().getName() : "Без бренду")
                .setPrice(coffee.getPrice())
                .setQuantity(quantity)
                .setPhotoPath(coffee.getPhotoPath())
                .setFormat(coffee.getProductFormat() != null ? coffee.getProductFormat().getDisplayName() : "В зернах")
                // Безпечний мапінг оцінок (якщо null, записуємо порожній рядок або null)
                .setBitterness(coffee.getBitterness() != null ? coffee.getBitterness().displayName : null)
                .setIntensity(coffee.getIntensity() != null ? coffee.getIntensity().displayName : null)
                .setComposition(coffee.getComposition() != null ? coffee.getComposition().getDisplayName() : null)
                .setRoastLevel(coffee.getRoastLevel() != null ? coffee.getRoastLevel().displayName : null)
                .setCapsuleSystem(coffee.getCapsuleSystem() != null ? coffee.getCapsuleSystem().getDisplayName() : null)
                .setCapsuleCount(coffee.getCapsuleCount() != null ? coffee.getCapsuleCount() + " шт." : null)
                // Безпечний мапінг ваги
                .setWeight(coffee.getWeight() != null ? coffee.getWeight().getDisplayName() : "Не вказано");


        cartService.addToCart(item);
        return "redirect:/cart/view";
    }

    @PostMapping("/update/{coffeeId}")
    public String updateQuantity(@PathVariable Long coffeeId,
                                 @RequestParam Integer quantity) {
        cartService.updateQuantity(coffeeId, quantity);
        return "redirect:/cart/view";
    }


    @GetMapping("/view")
    public String viewCart(Model model) {
        // 1. Отримуємо дані з сервісів
        Cart cart = cartService.getCart();
        DiscountSettings settings = discountService.getSettings();

        // 2. Викликаємо розрахунки, які вже лежать у CartService
        double subtotal = cartService.getTotal();
        BigDecimal total = cartService.getTotalWithDiscount();

        // 3. Визначаємо, чи була застосована знижка (для відображення в UI)
        double discountPercent = 0;
        if (settings != null && settings.isActive() && subtotal >= settings.getThreshold()) {
            discountPercent = settings.getDiscountPercent();
        }

        // 4. Передаємо все в модель
        model.addAttribute("cart", cart);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("total", total.doubleValue());
        model.addAttribute("discountPercent", discountPercent);
        model.addAttribute("discountSettings", settings);

        return "cart-view";
    }

    @PostMapping("/remove/{coffeeId}")
    public String removeFromCart(@PathVariable Long coffeeId) {
        cartService.removeFromCart(coffeeId);
        return "redirect:/cart/view";
    }
    @GetMapping("/api/count")
    @ResponseBody // Повертає просто дані (число), а не HTML-шаблон
    public long getCartCountApi() {
        return cartService.getCartItemCount();
    }
}
