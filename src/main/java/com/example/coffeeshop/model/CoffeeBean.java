package com.example.coffeeshop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class CoffeeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // id — унікальний ідентифікатор

    private String name;
    // name — назва кави

    @Column(length = 2000)
    private String description;
    // description — опис кави

    @Enumerated(EnumType.STRING)
    private RoastLevel roastLevel;
    // roastLevel — рівень обсмаження

    @Enumerated(EnumType.STRING)
    private Bitterness bitterness;
    // bitterness — гіркота

    @Enumerated(EnumType.STRING)
    private Acidity acidity;
    // acidity — кислотність

    @Enumerated(EnumType.STRING)
    private Composition composition;
    // composition — склад (арабіка/робуста)

    @Enumerated(EnumType.STRING)
    private Intensity intensity;
    // intensity — інтенсивність кави

    private Double price;
    // price — ціна

    private String photoPath;
    // photoPath — шлях до фото (uploads/filename.jpg)

    @ManyToOne
    @JoinColumn(name = "origin_country_id") // Зовнішній ключ для країни походження
    private OriginCountry originCountry;
    // originCountry — країна походження (вибірка з довідника)

    @ManyToOne
    @JoinColumn(name = "brand_id") // Зовнішній ключ для бренду
    private Brand brand;
    // brand — бренд кави (вибірка з довідника)
    @Enumerated(EnumType.STRING)
    private ProductWeight weight;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoastLevel getRoastLevel() {
        return roastLevel;
    }

    public void setRoastLevel(RoastLevel roastLevel) {
        this.roastLevel = roastLevel;
    }

    public Bitterness getBitterness() {
        return bitterness;
    }

    public void setBitterness(Bitterness bitterness) {
        this.bitterness = bitterness;
    }

    public Acidity getAcidity() {
        return acidity;
    }

    public void setAcidity(Acidity acidity) {
        this.acidity = acidity;
    }

    public Composition getComposition() {
        return composition;
    }

    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Intensity getIntensity() {
        return intensity;
    }

    public void setIntensity(Intensity intensity) {
        this.intensity = intensity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public OriginCountry getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(OriginCountry originCountry) {
        this.originCountry = originCountry;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}

