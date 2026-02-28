package com.example.coffeeshop.repository;

import jakarta.transaction.*;
import com.example.coffeeshop.model.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
    @Query("SELECT c FROM CoffeeBean c WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.brand.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND c.active = true")
    List<CoffeeBean> findByNameContainingIgnoreCaseAndActiveTrue(@Param("keyword") String name);
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
    @Modifying
    @Query("UPDATE CoffeeBean c SET c.price = ROUND(c.price * :multiplier, 0)")
    void bulkUpdatePrices(@Param("multiplier") Double multiplier);
    @Modifying
    @Transactional
    @Query("UPDATE CoffeeBean c SET c.active = :value WHERE c.id = :id")
    void updateAvailable(@Param("id") Long id, @Param("value") boolean value);

    @Modifying
    @Transactional
    @Query("UPDATE CoffeeBean c SET c.isHit = :value WHERE c.id = :id")
    void updateHit(@Param("id") Long id, @Param("value") boolean value);

    @Modifying
    @Transactional
    @Query("UPDATE CoffeeBean c SET c.isPromo = :value WHERE c.id = :id")
    void updatePromotion(@Param("id") Long id, @Param("value") boolean value);

    @Query("SELECT c FROM CoffeeBean c WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.brand.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND c.active = true")
    Page<CoffeeBean> searchActive(@Param("keyword") String keyword, Pageable pageable);




    // Додаткові методи можна буде додати пізніше (наприклад, пошук по назві)
}
