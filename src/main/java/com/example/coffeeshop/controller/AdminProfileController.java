package com.example.coffeeshop.controller;

import com.example.coffeeshop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/profile")
public class AdminProfileController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "admin/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            java.security.Principal principal,
            RedirectAttributes redirectAttributes) {

        String username = principal.getName();

        // 1. Перевірка старого пароля
        if (!userDetailsService.checkOldPassword(username, oldPassword)) {
            redirectAttributes.addFlashAttribute("error", "Старий пароль невірний");
            return "redirect:/admin/profile/change-password";
        }

        // 2. Перевірка збігу нових паролів
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Нові паролі не збігаються");
            return "redirect:/admin/profile/change-password";
        }

        // 3. Оновлення
        userDetailsService.updatePassword(username, newPassword);
        redirectAttributes.addFlashAttribute("success", "Пароль успішно змінено");
        
        return "redirect:/admin/profile/change-password";
    }
}