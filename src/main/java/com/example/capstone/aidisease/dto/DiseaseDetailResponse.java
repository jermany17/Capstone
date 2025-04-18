package com.example.capstone.aidisease.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DiseaseDetailResponse {
    private String disease;
    private List<String> imageUrls;
    private String definition;
    private String cause;
    private String symptom;
    private String source;
}
