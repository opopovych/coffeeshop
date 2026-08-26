package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.model.dto.ProductSelectDto;
import jakarta.transaction.*;
import com.example.coffeeshop.mapper.CoffeeBeanMapper;
import com.example.coffeeshop.model.*;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import com.example.coffeeshop.repository.CoffeeBeanRepository;
import com.example.coffeeshop.service.CoffeeBeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        return coffeeBeanRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
        return coffeeBeanRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
    }

    @Override
    public List<CoffeeBean> findByBrandId(Long brandId) {
        return coffeeBeanRepository.findByBrandIdAndActiveTrue(brandId);
    }

    @Override
    public Page<CoffeeBean> filter(
            ProductFormat format, // Оновлено тип
            Long brandId,
            Long countryId,
            List<Intensity> intensity,
            List<RoastLevel> roast,
            List<Bitterness> bitterness,
            List<Composition> composition,
            List<Acidity> acidity,
            CapsuleSystem capsuleSystem, Integer capsuleCount,
            Pageable pageable
    ) {
        return coffeeBeanRepository.filter(
                brandId, countryId, format, intensity, roast, bitterness, composition, acidity, capsuleSystem, capsuleCount,pageable
        );
    }

    @Override
    public List<CoffeeBean> findAllById(List<Long> productIds) {
        return coffeeBeanRepository.findAllById(productIds);
    }

    @Override
    public void saveAll(List<CoffeeBean> products) {
coffeeBeanRepository.saveAll(products);
    }

    @Override
    public void deleteAllById(List<Long> productIds) {
coffeeBeanRepository.deleteAllById(productIds);
    }

    @Override
    public Page<CoffeeBean> findAllActive(Pageable pageable) {
        return coffeeBeanRepository.findAllActive(pageable);
    }
    /*@Override
    public Page<CoffeeBean> findAllActiveRandom(Pageable pageable){
        return coffeeBeanRepository.findAllActiveById(pageable);
    }*/
    @Override
    public Page<CoffeeBean> findAllActiveRandom(Pageable pageable) {
        // Просто повертаємо активні товари з динамічним сортуванням, яке прийшло з контролера
        return coffeeBeanRepository.findAllActive(pageable);
    }
    @Override
    @Transactional
    public void adjustAllPrices(Double percent) {
        // Один запит до бази, який оновлює все за мілісекунди
        coffeeBeanRepository.bulkUpdatePrices(1 + (percent / 100));
    }

    @Override
    @Transactional
    public void updateSingleField(Long id, String field, boolean value) {
        if ("active".equals(field)) coffeeBeanRepository.updateAvailable(id, value);
        else if ("isHit".equals(field)) coffeeBeanRepository.updateHit(id, value);
        else if ("promo".equals(field)) coffeeBeanRepository.updatePromotion(id, value);

    }
    @Override
    public Page<CoffeeBean> search(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            // Якщо запит порожній, повертаємо просто всі активні товари
            return coffeeBeanRepository.findAllActive(pageable);
        }
        // Викликаємо наш новий метод з репозиторію
        return coffeeBeanRepository.searchActive(query.trim(), pageable);
    }
    public List<ProductSelectDto> findAllForSelect() {
        return coffeeBeanRepository.findAllForSelect();
    }

}
