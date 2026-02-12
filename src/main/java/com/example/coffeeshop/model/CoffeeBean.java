package com.example.coffeeshop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
public class CoffeeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // id — унікальний ідентифікатор
    private boolean active = true;

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

    private boolean isHit = false;

    private boolean isPromo = false;

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
    @Column(unique = true)
    private String sku;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

