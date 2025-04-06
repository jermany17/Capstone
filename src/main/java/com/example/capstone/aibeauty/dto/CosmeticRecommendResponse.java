package com.example.capstone.aibeauty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CosmeticRecommendResponse {

    private String category;
    private String score;

    private String brand1;
    private String product1;
    private String product1Image;
    private int product1Price;
    private String product1Link;

    private String brand2;
    private String product2;
    private String product2Image;
    private int product2Price;
    private String product2Link;

    private String brand3;
    private String product3;
    private String product3Image;
    private int product3Price;
    private String product3Link;

    private String brand4;
    private String product4;
    private String product4Image;
    private int product4Price;
    private String product4Link;

    private String brand5;
    private String product5;
    private String product5Image;
    private int product5Price;
    private String product5Link;
}
