package com.example.coffeeshop.mapper;

import com.example.coffeeshop.model.Bitterness;
import com.example.coffeeshop.model.CoffeeBean;
import com.example.coffeeshop.model.dto.CoffeeBeanDto;
import org.springframework.stereotype.Component;

@Component
public class CoffeeBeanMapper {

    public CoffeeBeanDto toDto(CoffeeBean bean) {
        return new CoffeeBeanDto()
                .setName(bean.getName())
                .setPhotoPath(bean.getPhotoPath())
                .setPrice(bean.getPrice())
                .setIntensity(bean.getIntensity().name())
                .setComposition(bean.getComposition().name())
                .setBitterness(bean.getBitterness().name())
                .setRoastLevel(bean.getRoastLevel().name())
                .setBrand(bean.getBrand().getName());
    }
}
