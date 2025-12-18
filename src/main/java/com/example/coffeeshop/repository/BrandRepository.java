package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand,Long> {
}
