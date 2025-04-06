package com.example.capstone.aibeauty.controller;

import com.example.capstone.aibeauty.dto.CosmeticRecommendResponse;
import com.example.capstone.aibeauty.dto.TotalScoreRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.capstone.aibeauty.service.CosmeticRecommendService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CosmeticRecommendController {

    private final CosmeticRecommendService cosmeticRecommendService;

    @PostMapping("/cosmetic-recommend")
    public ResponseEntity<List<CosmeticRecommendResponse>> recommend(@RequestBody TotalScoreRequest request) {
        // 추천 화장품 정보 반환
        // JSON 형태의 요청 본문을 TotalScoreRequest 객체로 매핑하여 받음 (total 값들)

        List<CosmeticRecommendResponse> response = cosmeticRecommendService.recommend(request);
        return ResponseEntity.ok(response);
    }
}
