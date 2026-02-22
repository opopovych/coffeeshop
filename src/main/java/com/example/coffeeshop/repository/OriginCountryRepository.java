package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.OriginCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OriginCountryRepository extends JpaRepository<OriginCountry,Long> {
    // Знайти всі країни, які закріплені за товарами цього бренду
    @Query("SELECT DISTINCT cb.originCountry FROM CoffeeBean cb WHERE cb.brand.id = :brandId AND cb.active = true")
    List<OriginCountry> findCountriesByBrandId(@Param("brandId") Long brandId);
}
