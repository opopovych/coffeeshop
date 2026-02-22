package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand,Long> {
    long count();
    // Знайти всі бренди, у яких є хоча б один товар з цією країною
    @Query("SELECT DISTINCT cb.brand FROM CoffeeBean cb WHERE cb.originCountry.id = :countryId AND cb.active = true")
    List<Brand> findBrandsByCountryId(@Param("countryId") Long countryId);
    @Query("SELECT DISTINCT b FROM Brand b JOIN CoffeeBean cb ON cb.brand = b WHERE cb.active = true")
    List<Brand> findAllWithActiveProducts();
}
