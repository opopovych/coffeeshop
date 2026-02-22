package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.Brand;
import com.example.coffeeshop.repository.BrandRepository;
import com.example.coffeeshop.service.BrandService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Brand not found: " + id));
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }
    @Override
    public List<Brand> getBrandsByCountry(Long countryId) {
        if (countryId == null) {
            return brandRepository.findAll();
        }
        return brandRepository.findBrandsByCountryId(countryId);
    }
}
