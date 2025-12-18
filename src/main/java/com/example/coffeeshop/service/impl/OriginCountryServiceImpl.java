package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.OriginCountry;
import com.example.coffeeshop.repository.OriginCountryRepository;
import com.example.coffeeshop.service.OriginCountryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OriginCountryServiceImpl implements OriginCountryService {

    @Autowired
    private OriginCountryRepository originCountryRepository;
    @Override
    public OriginCountry save(OriginCountry originCountry) {
        return originCountryRepository.save(originCountry);
    }

    @Override
    public OriginCountry findById(Long id) {
        return originCountryRepository.findById(id).orElseThrow(() -> new RuntimeException("Country not found: " + id));
    }

    @Override
    public List<OriginCountry> findAll() {
        return originCountryRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        originCountryRepository.deleteById(id);
    }
}
