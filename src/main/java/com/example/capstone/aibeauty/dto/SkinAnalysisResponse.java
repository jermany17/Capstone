package com.example.capstone.aibeauty.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SkinAnalysisResponse {
    private List<String> imageUrls;

    private int skinAge;
    private int foreheadWrinkle;
    private int foreheadPigmentation;
    private int glabellaWrinkle;
    private int lefteyeWrinkle;
    private int righteyeWrinkle;
    private int leftcheekPigmentation;
    private int leftcheekPore;
    private int rightcheekPigmentation;
    private int rightcheekPore;
    private int lipDryness;
    private int jawlineSagging;

    private int totalWrinkle;
    private int totalPigmentation;
    private int totalPore;

    private LocalDateTime createdAt;
}
