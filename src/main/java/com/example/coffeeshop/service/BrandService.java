package com.example.coffeeshop.service;

import com.example.coffeeshop.model.Brand;
import java.util.List;

public interface BrandService {
    Brand save(Brand brand);        // створити або оновити Бренд

    Brand findById(Long id);                  // знайти по id

    List<Brand> findAll();                    // всі Бренди

    void delete(Long id);                          // видалити Бренд
}
