package com.example.capstone.aibeauty.controller;

import com.example.capstone.aibeauty.dto.SkinAnalysisRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.capstone.aibeauty.service.SkinAnalysisService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-analysis")
public class SkinAnalysisController {

    private final SkinAnalysisService skinAnalysisService;

    // @ModelAttribute = 객체 매핑
    @PostMapping("/submit")
    public ResponseEntity<?> submitAnalysis(@ModelAttribute SkinAnalysisRequest request, Authentication authentication) {
        // 피부 분석하고 분석 결과 고유 id 반환
        try {
            String analysisId = skinAnalysisService.analyze(request, authentication);
            return ResponseEntity.ok(Map.of("analysisId", analysisId)); //분석 결과 고유 id 반환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "요청 처리 중 오류가 발생했습니다.a"));
        }
    }
}