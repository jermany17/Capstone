package com.example.capstone.aibeauty.controller;

import com.example.capstone.aibeauty.dto.SkinAnalysisResponse;
import com.example.capstone.aibeauty.service.SkinAnalysisResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/skin-analysis")
public class SkinAnalysisResultController {

    private final SkinAnalysisResultService resultService;

    @GetMapping("/{analysisId}")
    public ResponseEntity<SkinAnalysisResponse> getResult(@PathVariable String analysisId) {
        // 분석 결과 고유 id를 받아 결과 반환
        SkinAnalysisResponse response = resultService.getAnalysisResult(analysisId);
        return ResponseEntity.ok(response);
    }
}
