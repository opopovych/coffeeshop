package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
    List<CoffeeBean> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    List<CoffeeBean> findByBrandIdAndActiveTrue(Long brandId);
    @Query("""
    SELECT c FROM CoffeeBean c
    WHERE c.active = true
      AND (:brandId IS NULL OR c.brand.id = :brandId)
      AND (:countryId IS NULL OR c.originCountry.id = :countryId)
      AND (:intensity IS NULL OR c.intensity IN :intensity)
      AND (:roast IS NULL OR c.roastLevel IN :roast)
      AND (:bitterness IS NULL OR c.bitterness IN :bitterness)
      AND (:composition IS NULL OR c.composition IN :composition)
      AND (:acidity IS NULL OR c.acidity IN :acidity)
""")
    Page<CoffeeBean> filter(
            @Param("brandId") Long brandId,
            @Param("countryId") Long countryId,
            @Param("intensity") List<Intensity> intensity, // Змінено на List
            @Param("roast") List<RoastLevel> roast,         // Змінено на List
            @Param("bitterness") List<Bitterness> bitterness, // Змінено на List
            @Param("composition") List<Composition> composition, // Змінено на List
            @Param("acidity") List<Acidity> acidity,
            Pageable pageable
    );
    long count();
    @Query("SELECT c FROM CoffeeBean c WHERE c.active = true")
    Page<CoffeeBean> findAllActive(Pageable pageable);
    @Query(value = "SELECT * FROM coffee_bean WHERE active = true ORDER BY id DESC", nativeQuery = true)
    Page<CoffeeBean> findAllActiveById(Pageable pageable);




    // Додаткові методи можна буде додати пізніше (наприклад, пошук по назві)
}
