package com.example.capstone.aibeauty.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalScoreRequest {
    private int totalWrinkle;
    private int totalPigmentation;
    private int totalPore;
}