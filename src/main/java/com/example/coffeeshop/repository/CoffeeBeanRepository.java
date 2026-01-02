package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
    List<CoffeeBean> findByNameContainingIgnoreCase (String name);
    List<CoffeeBean> findByBrandId(Long brandId);
    @Query("""
    SELECT c FROM CoffeeBean c
    WHERE (:brandId IS NULL OR c.brand.id = :brandId)
      AND (:intensity IS NULL OR c.intensity = :intensity)
      AND (:roast IS NULL OR c.roastLevel = :roast)
      AND (:bitterness IS NULL OR c.bitterness = :bitterness)
      AND (:composition IS NULL OR c.composition = :composition)
""")
    Page<CoffeeBean> filter(
            @Param("brandId") Long brandId,
            @Param("intensity") Intensity intensity,
            @Param("roast") RoastLevel roast,
            @Param("bitterness") Bitterness bitterness,
            @Param("composition") Composition composition,
            Pageable pageable
    );
    long count();




    // Додаткові методи можна буде додати пізніше (наприклад, пошук по назві)
}
