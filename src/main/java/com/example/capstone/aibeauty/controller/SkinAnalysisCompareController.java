package com.example.capstone.aibeauty.controller;

import com.example.capstone.aibeauty.dto.SkinAnalysisCompareResponse;
import com.example.capstone.aibeauty.dto.SkinAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.capstone.aibeauty.service.SkinAnalysisResultService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-analysis")
public class SkinAnalysisCompareController {

    private final SkinAnalysisResultService resultService;

    @GetMapping("/compare")
    public ResponseEntity<SkinAnalysisCompareResponse> compare(
            @RequestParam String analysisId1,
            @RequestParam String analysisId2) {
        // 쿼리 파라미터로 두 개의 분석 id를 받아 각 결과를 조회하고 평균 점수를 계산해서 반환

        SkinAnalysisResponse r1 = resultService.getAnalysisResult(analysisId1);
        SkinAnalysisResponse r2 = resultService.getAnalysisResult(analysisId2);

        double avg1 = calculateAverageScore(r1); // 분석 id1 평균점수
        double avg2 = calculateAverageScore(r2); // 분석 id2 평균점수

        return ResponseEntity.ok(
                SkinAnalysisCompareResponse.builder()
                        .result1(r1)
                        .result2(r2)
                        .result1Average(avg1)
                        .result2Average(avg2)
                        .build()
        );
    }

    // 평균 점수 계산 메서드 (total 값 3개 빼고 19개 항목으로 계산)
    private double calculateAverageScore(SkinAnalysisResponse res) {
        int sum =
                res.getForeheadWrinkle() + res.getForeheadPigmentation() +
                        res.getForeheadMoisture() + res.getForeheadElasticity() +
                        res.getGlabellaWrinkle() + res.getLefteyeWrinkle() + res.getRighteyeWrinkle() +
                        res.getLeftcheekPigmentation() + res.getLeftcheekPore() +
                        res.getLeftcheekMoisture() + res.getLeftcheekElasticity() +
                        res.getRightcheekPigmentation() + res.getRightcheekPore() +
                        res.getRightcheekMoisture() + res.getRightcheekElasticity() +
                        res.getLipDryness() + res.getJawlineSagging() +
                        res.getChinMoisture() + res.getChinElasticity();

        return Math.round((sum / 19.0) * 10.0) / 10.0;  // 소수점 1자리 반올림
    }
}
