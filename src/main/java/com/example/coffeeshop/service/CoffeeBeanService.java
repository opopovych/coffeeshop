package com.example.coffeeshop.service;

import com.example.coffeeshop.model.*;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoffeeBeanService {

    CoffeeBean save(CoffeeBean coffeeBean);        // створити або оновити товар

    CoffeeBean findById(Long id);                  // знайти по id

    List<CoffeeBean> findAll();                    // всі товари

    List<CoffeeBeanDto> findAllDto();                    // всі товари

    void delete(Long id);                          // видалити товар

    List<CoffeeBean> search (String query);
    List<CoffeeBean> findByBrandId(Long brandId);
    Page<CoffeeBean> filter(
            Long brandId,
            Long countryId,
            List<Intensity> intensity,
            List<RoastLevel> roast,
            List<Bitterness> bitterness,
            List<Composition> composition,
            List<Acidity> acidity,
            Pageable pageable
    );


    List<CoffeeBean> findAllById(List<Long> productIds);

    void saveAll(List<CoffeeBean> products);

    void deleteAllById(List<Long> productIds);

    Page<CoffeeBean> findAllActive(Pageable pageable);
}
