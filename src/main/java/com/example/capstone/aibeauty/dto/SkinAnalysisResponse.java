package com.example.capstone.aibeauty.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SkinAnalysisResponse {
    private List<String> imageUrls;

    private int foreheadWrinkle;
    private int foreheadPigmentation;
    private int foreheadMoisture;
    private int foreheadElasticity;
    private int glabellaWrinkle;
    private int lefteyeWrinkle;
    private int righteyeWrinkle;
    private int leftcheekPigmentation;
    private int leftcheekPore;
    private int leftcheekMoisture;
    private int leftcheekElasticity;
    private int rightcheekPigmentation;
    private int rightcheekPore;
    private int rightcheekMoisture;
    private int rightcheekElasticity;
    private int lipDryness;
    private int jawlineSagging;
    private int chinMoisture;
    private int chinElasticity;

    private int totalWrinkle;
    private int totalPigmentation;
    private int totalPore;
    private int totalMoisture;
    private int totalElasticity;

    private LocalDateTime createdAt;
}
