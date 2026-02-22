package com.example.coffeeshop.service;

import java.util.List;
import java.util.Map;

public interface NovaPoshtaService {
    List<Map<String, Object>> getCities(String cityName);
    List<Map<String, Object>> getWarehouses(String cityRef);
}
