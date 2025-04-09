package com.example.capstone.aibeauty.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cosmetic_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CosmeticProduct {
    // DB에 화장품 이렇게 저장해서 코드 긴 거 양해좀요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String category; //모공, 주름, 색소침착

    @Column(length = 10, nullable = false)
    private String score; // high or low

    // 제품 1
    @Column(name = "brand1", nullable = false, length = 50)
    private String brand1;

    @Column(name = "product1", nullable = false, length = 100)
    private String product1;

    @Column(name = "product1_image", nullable = false, length = 1000)
    private String product1Image;

    @Column(name = "product1_price", nullable = false)
    private int product1Price;

    @Column(name = "product1_link", nullable = false, length = 1000)
    private String product1Link;

    // 제품 2
    @Column(name = "brand2", nullable = false, length = 50)
    private String brand2;

    @Column(name = "product2", nullable = false, length = 100)
    private String product2;

    @Column(name = "product2_image", nullable = false, length = 1000)
    private String product2Image;

    @Column(name = "product2_price", nullable = false)
    private int product2Price;

    @Column(name = "product2_link", nullable = false, length = 1000)
    private String product2Link;

    // 제품 3
    @Column(name = "brand3", nullable = false, length = 50)
    private String brand3;

    @Column(name = "product3", nullable = false, length = 100)
    private String product3;

    @Column(name = "product3_image", nullable = false, length = 1000)
    private String product3Image;

    @Column(name = "product3_price", nullable = false)
    private int product3Price;

    @Column(name = "product3_link", nullable = false, length = 1000)
    private String product3Link;

    // 제품 4
    @Column(name = "brand4", nullable = false, length = 50)
    private String brand4;

    @Column(name = "product4", nullable = false, length = 100)
    private String product4;

    @Column(name = "product4_image", nullable = false, length = 1000)
    private String product4Image;

    @Column(name = "product4_price", nullable = false)
    private int product4Price;

    @Column(name = "product4_link", nullable = false, length = 1000)
    private String product4Link;

    // 제품 5
    @Column(name = "brand5", nullable = false, length = 50)
    private String brand5;

    @Column(name = "product5", nullable = false, length = 100)
    private String product5;

    @Column(name = "product5_image", nullable = false, length = 1000)
    private String product5Image;

    @Column(name = "product5_price", nullable = false)
    private int product5Price;

    @Column(name = "product5_link", nullable = false, length = 1000)
    private String product5Link;

    @Builder
    public CosmeticProduct(String category, String score,
                           String brand1, String product1, String product1Image, int product1Price, String product1Link,
                           String brand2, String product2, String product2Image, int product2Price, String product2Link,
                           String brand3, String product3, String product3Image, int product3Price, String product3Link,
                           String brand4, String product4, String product4Image, int product4Price, String product4Link,
                           String brand5, String product5, String product5Image, int product5Price, String product5Link) {

        this.category = category;
        this.score = score;

        this.brand1 = brand1;
        this.product1 = product1;
        this.product1Image = product1Image;
        this.product1Price = product1Price;
        this.product1Link = product1Link;

        this.brand2 = brand2;
        this.product2 = product2;
        this.product2Image = product2Image;
        this.product2Price = product2Price;
        this.product2Link = product2Link;

        this.brand3 = brand3;
        this.product3 = product3;
        this.product3Image = product3Image;
        this.product3Price = product3Price;
        this.product3Link = product3Link;

        this.brand4 = brand4;
        this.product4 = product4;
        this.product4Image = product4Image;
        this.product4Price = product4Price;
        this.product4Link = product4Link;

        this.brand5 = brand5;
        this.product5 = product5;
        this.product5Image = product5Image;
        this.product5Price = product5Price;
        this.product5Link = product5Link;
    }
}