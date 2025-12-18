package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.mapper.CoffeeBeanMapper;
import com.example.coffeeshop.model.*;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import com.example.coffeeshop.repository.CoffeeBeanRepository;
import com.example.coffeeshop.service.CoffeeBeanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeBeanServiceImpl implements CoffeeBeanService {
    @Autowired
    private  CoffeeBeanRepository coffeeBeanRepository;
    @Autowired
    private CoffeeBeanMapper mapper;

    @Override
    public CoffeeBean save(CoffeeBean coffeeBean) {
        return coffeeBeanRepository.save(coffeeBean);
    }

    @Override
    public CoffeeBean findById(Long id) {
        return coffeeBeanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CoffeeBean not found: " + id));
    }

    @Override
    public List<CoffeeBean> findAll() {
        return coffeeBeanRepository.findAll();
    }

    @Override
    public List<CoffeeBeanDto> findAllDto() {

        return coffeeBeanRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        coffeeBeanRepository.deleteById(id);
    }

    @Override
    public List<CoffeeBean> search(String query) {
        if (query == null || query.isBlank()){
            return coffeeBeanRepository.findAll();
        }
        return coffeeBeanRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public List<CoffeeBean> findByBrandId(Long brandId) {
        return coffeeBeanRepository.findByBrandId(brandId);
    }

    @Override
    public Page<CoffeeBean> filter(
            Long brandId,
            Intensity intensity,
            RoastLevel roast,
            Bitterness bitterness,
            Composition composition,
            Pageable pageable
    ) {
        return coffeeBeanRepository.filter(
                brandId, intensity, roast, bitterness, composition, pageable
        );
    }



}
