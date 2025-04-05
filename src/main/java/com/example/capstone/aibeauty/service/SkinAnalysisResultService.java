package com.example.capstone.aibeauty.service;

import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import com.example.capstone.aibeauty.dto.SkinAnalysisResponse;
import com.example.capstone.aibeauty.repository.SkinAnalysisResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkinAnalysisResultService {

    private final SkinAnalysisResultRepository repository;

    // 분석 결과 id를 기반으로 DB에서 데이터를 조회하고 이를 응답 DTO로 변환하여 반환
    public SkinAnalysisResponse getAnalysisResult(String analysisId) {
        SkinAnalysisResult result = repository.findByAnalysisId(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("해당 분석 결과를 찾을 수 없습니다."));

        return SkinAnalysisResponse.builder()
                .imageUrls(result.getImageUrls())
                .foreheadWrinkle(result.getForeheadWrinkle())
                .foreheadPigmentation(result.getForeheadPigmentation())
                .foreheadMoisture(result.getForeheadMoisture())
                .foreheadElasticity(result.getForeheadElasticity())
                .glabellaWrinkle(result.getGlabellaWrinkle())
                .lefteyeWrinkle(result.getLefteyeWrinkle())
                .righteyeWrinkle(result.getRighteyeWrinkle())
                .leftcheekPigmentation(result.getLeftcheekPigmentation())
                .leftcheekPore(result.getLeftcheekPore())
                .leftcheekMoisture(result.getLeftcheekMoisture())
                .leftcheekElasticity(result.getLeftcheekElasticity())
                .rightcheekPigmentation(result.getRightcheekPigmentation())
                .rightcheekPore(result.getRightcheekPore())
                .rightcheekMoisture(result.getRightcheekMoisture())
                .rightcheekElasticity(result.getRightcheekElasticity())
                .lipDryness(result.getLipDryness())
                .jawlineSagging(result.getJawlineSagging())
                .chinMoisture(result.getChinMoisture())
                .chinElasticity(result.getChinElasticity())
                .totalWrinkle(result.getTotalWrinkle())
                .totalPigmentation(result.getTotalPigmentation())
                .totalPore(result.getTotalPore())
                .createdAt(result.getCreatedAt())
                .build();
    }
}
