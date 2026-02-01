package com.example.coffeeshop.config;

import com.example.coffeeshop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // Конструктор для ін'єкції твого сервісу
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Доступ до адмінки для будь-якого залогіненого користувача
                        .requestMatchers("/admin/**").authenticated()
                        // Відкриті шляхи для клієнтів
                        .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")            // URL, на який відправляється запит
                        .logoutSuccessUrl("/")           // Куди перекинути після виходу (на головну)
                        .invalidateHttpSession(true)     // Видалити сесію
                        .deleteCookies("JSESSIONID")     // Очистити куки в браузері
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Тимчасово вимкнено для спрощення роботи з формами

        return http.build();
    }
}