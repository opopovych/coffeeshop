package com.example.coffeeshop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                // Спочатку шукаємо у зовнішній папці uploads (file:uploads/)
                // Потім шукаємо у стандартній папці ресурсів (classpath:/static/images/)
                .addResourceLocations("file:uploads/", "classpath:/static/images/");
    }
}
