package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.service.NovaPoshtaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NovaPoshtaServiceImpl implements NovaPoshtaService {

    @Value("${novaposhta.api.key}")
    private String apiKey;

    @Value("${novaposhta.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Метод для пошуку міст за назвою
    public List<Map<String, Object>> getCities(String cityName) {
        Map<String, Object> request = new HashMap<>();
        request.put("apiKey", apiKey);
        request.put("modelName", "Address");
        request.put("calledMethod", "getCities");
        
        Map<String, String> methodProps = new HashMap<>();
        methodProps.put("FindByString", cityName);
        request.put("methodProperties", methodProps);

        Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);
        return (List<Map<String, Object>>) response.get("data");
    }

    // Метод для отримання відділень у місті
    public List<Map<String, Object>> getWarehouses(String cityRef) {
        Map<String, Object> request = new HashMap<>();
        request.put("apiKey", apiKey);
        request.put("modelName", "Address");
        request.put("calledMethod", "getWarehouses");

        Map<String, String> methodProps = new HashMap<>();
        methodProps.put("CityRef", cityRef);
        // Додаємо ліміт, щоб API не "зависало" на великих списках
        methodProps.put("Limit", "500");
        request.put("methodProperties", methodProps);

        try {
            Map<String, Object> response = restTemplate.postForObject(apiUrl, request, Map.class);

            // Перевірка: чи прийшла відповідь і чи немає в ній помилок від НП
            if (response != null && Boolean.TRUE.equals(response.get("success"))) {
                return (List<Map<String, Object>>) response.get("data");
            } else if (response != null) {
                System.err.println("Помилка НП: " + response.get("errors"));
            }
        } catch (Exception e) {
            System.err.println("Помилка при запиті до Нової Пошти: " + e.getMessage());
        }

        return List.of(); // Повертаємо порожній список, щоб контролер не падав
    }
}