package com.example.capstone.aidisease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DiagnosisResultResponse {
    private String disease;
    private float probability;
    private String treatment;
    private String source;
    private String imageUrl;
}
