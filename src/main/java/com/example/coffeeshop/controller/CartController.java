package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.DiscountSettings;
import com.example.coffeeshop.model.dto.Cart;
import com.example.coffeeshop.model.dto.CartItem;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.impl.CartService;
import com.example.coffeeshop.service.impl.DiscountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CoffeeBeanService coffeeBeanService;
    private final CartService cartService;
    private final DiscountService discountService;

    public CartController(CoffeeBeanService coffeeBeanService, CartService cartService, DiscountService discountService) {
        this.coffeeBeanService = coffeeBeanService;
        this.cartService = cartService;
        this.discountService = discountService;
    }

    @PostMapping("/add/{coffeeId}")
    public String addToCart(@PathVariable Long coffeeId,
                            @RequestParam(defaultValue = "1") Integer quantity) {
        CoffeeBean coffee = coffeeBeanService.findById(coffeeId);
        CartItem item = new CartItem()
                .setCoffeeId(coffee.getId())
                .setName(coffee.getName())
                .setBrand(coffee.getBrand().getName())
                .setPrice(coffee.getPrice())
                .setQuantity(quantity)
                .setPhotoPath(coffee.getPhotoPath())
                .setBitterness(coffee.getBitterness().displayName)
                .setIntensity(coffee.getIntensity().displayName)
                .setComposition(coffee.getComposition().displayName)
                .setRoastLevel(coffee.getRoastLevel().displayName)
                .setWeight(coffee.getWeight().getDisplayName());
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
        Cart cart = cartService.getCart();
        double subtotal = cartService.getTotal();

        DiscountSettings settings = discountService.getSettings();

        double total = subtotal;
        double discountPercent = 0;

        // Додаємо settings.isActive() в перевірку
        if (settings != null && settings.getThreshold() != null && settings.isActive()) {
            if (subtotal >= settings.getThreshold()) {
                discountPercent = settings.getDiscountPercent();
                total = subtotal * (1 - (discountPercent / 100));
            }
            model.addAttribute("discountSettings", settings);
        } else {
            model.addAttribute("discountSettings", null);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("subtotal", subtotal);
        // Використовуємо Math.round для відображення цілого числа
        model.addAttribute("total", Math.round(total));
        model.addAttribute("discountPercent", discountPercent);

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
