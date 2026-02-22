package com.example.coffeeshop.service;

import com.example.coffeeshop.model.OriginCountry;
import java.util.List;

public interface OriginCountryService {
    OriginCountry save(OriginCountry originCountry);        // створити або оновити Країну

    OriginCountry findById(Long id);                  // знайти по id

    List<OriginCountry> findAll();                    // всі Країни

    void delete(Long id);                          // видалити Країну
    List<OriginCountry> getCountriesByBrand(Long brandId);
}
