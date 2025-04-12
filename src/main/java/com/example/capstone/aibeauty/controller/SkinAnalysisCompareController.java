package com.example.capstone.aibeauty.controller;

import com.example.capstone.aibeauty.dto.SkinAnalysisCompareResponse;
import com.example.capstone.aibeauty.dto.SkinAnalysisResponse;
import com.example.capstone.auth.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.capstone.aibeauty.service.SkinAnalysisResultService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-analysis")
public class SkinAnalysisCompareController {

    private final SkinAnalysisResultService resultService;

    @GetMapping("/date")
    public ResponseEntity<?> getAnalysisDates(Authentication authentication) {
        try {
            String userId = ((User) authentication.getPrincipal()).getUserId();
            List<LocalDate> dates = resultService.getAnalysisDates(userId);

            if (dates.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "피부 진단 결과가 존재하지 않습니다."));
            }

            return ResponseEntity.ok(Map.of("date", dates));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "날짜 목록을 불러오는 데 실패했습니다."));
        }
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compare(
            // 요청 파라미터로 LocalDate 형식의 date1, date2를 받음 (ex. 2025-03-01 형식으로 줘야 됨)
            @RequestParam("date1") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date1,
            @RequestParam("date2") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date2,
            Authentication authentication) {
        // 전달받은 두 날짜(date1, date2)에 해당하는 분석 결과 조회 및 평균 점수 반환 = 개선도 확인 목적

        try {
            // 현재 로그인 되어 있는 사용자의 userId 추출
            String userId = ((User) authentication.getPrincipal()).getUserId();

            // 로그인 되어 있는 userId의 각 날짜에 해당하는 분석 결과 조회
            SkinAnalysisResponse r1 = resultService.getAnalysisResultByDate(userId, date1);
            SkinAnalysisResponse r2 = resultService.getAnalysisResultByDate(userId, date2);

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
        } catch (IllegalArgumentException e) {
            // 날짜에 해당하는 분석 결과가 없는 경우
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 평균 점수 계산 메서드 (total 값 3개 빼고 11개 항목으로 계산)
    private double calculateAverageScore(SkinAnalysisResponse res) {
        int sum =
                res.getForeheadWrinkle() + res.getForeheadPigmentation() +
                        res.getGlabellaWrinkle() + res.getLefteyeWrinkle() + res.getRighteyeWrinkle() +
                        res.getLeftcheekPigmentation() + res.getLeftcheekPore() +
                        res.getRightcheekPigmentation() + res.getRightcheekPore() +
                        res.getLipDryness() + res.getJawlineSagging();

        return Math.round((sum / 11.0) * 10.0) / 10.0;  // 소수점 1자리 반올림
    }
}
