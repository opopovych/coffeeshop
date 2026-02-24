package com.example.coffeeshop.service;

import com.example.coffeeshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Додай цей ін'єкт

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.coffeeshop.model.User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Користувача не знайдено");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
    // У файл CustomUserDetailsService.java додай ці методи:

    public com.example.coffeeshop.model.User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void updatePassword(String username, String newPassword) {
        com.example.coffeeshop.model.User user = userRepository.findByUsername(username);
        if (user != null) {
            // Хешуємо новий пароль за допомогою BCrypt
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    public boolean checkOldPassword(String username, String oldPassword) {
        com.example.coffeeshop.model.User user = userRepository.findByUsername(username);
        if (user == null) return false;
        // Порівнюємо введений "сирий" пароль із хешованим у базі
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}