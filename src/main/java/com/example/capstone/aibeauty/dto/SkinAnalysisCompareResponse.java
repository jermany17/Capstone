package com.example.capstone.aibeauty.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SkinAnalysisCompareResponse {
    private SkinAnalysisResponse result1;
    private SkinAnalysisResponse result2;
    private double result1Average;
    private double result2Average;
}