package com.example.coffeeshop.controller;

import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.dto.CartItem;
import com.example.coffeeshop.service.CoffeeBeanService;
import com.example.coffeeshop.service.impl.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CoffeeBeanService coffeeBeanService;
    private final CartService cartService;

    public CartController(CoffeeBeanService coffeeBeanService, CartService cartService) {
        this.coffeeBeanService = coffeeBeanService;
        this.cartService = cartService;
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
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        return "cart-view";
    }

    @PostMapping("/remove/{coffeeId}")
    public String removeFromCart(@PathVariable Long coffeeId) {
        cartService.removeFromCart(coffeeId);
        return "redirect:/cart/view";
    }
}
