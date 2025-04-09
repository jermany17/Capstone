package com.example.capstone.aibeauty.service;

import com.example.capstone.aibeauty.domain.CosmeticProduct;
import com.example.capstone.aibeauty.dto.CosmeticRecommendResponse;
import com.example.capstone.aibeauty.dto.TotalScoreRequest;
import com.example.capstone.aibeauty.repository.CosmeticProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CosmeticRecommendService {

    private final CosmeticProductRepository productRepository;

    // 총합 점수를 기반으로 5가지 카테고리(주름, 색소침착, 모공, 수분, 탄력)에 대해 추천 화장품을 가져옴
    // 각 항목은 점수에 따라 "low" 또는 "high"
    public List<CosmeticRecommendResponse> recommend(TotalScoreRequest request) {
        return List.of(
                getRecommendation("주름", request.getTotalWrinkle() <= 13 ? "low" : "high"),
                getRecommendation("색소침착", request.getTotalPigmentation() <= 7 ? "low" : "high"),
                getRecommendation("모공", request.getTotalPore() <= 5 ? "low" : "high"),
                getRecommendation("수분", request.getTotalMoisture() <= 20 ? "low" : "high"),
                getRecommendation("탄력", request.getTotalElasticity() <= 20 ? "low" : "high")
        );
    }

    // 특정 카테고리와 점수 상태에 따라 DB에서 추천 화장품 정보를 가져오고 응답 DTO로 변환
    private CosmeticRecommendResponse getRecommendation(String category, String score) {
        CosmeticProduct product = productRepository.findByCategoryAndScore(category, score)
                .orElseThrow(() -> new RuntimeException("추천 화장품 정보가 없습니다."));

        return CosmeticRecommendResponse.builder()
                .category(product.getCategory())
                .score(product.getScore())

                .brand1(product.getBrand1())
                .product1(product.getProduct1())
                .product1Image(product.getProduct1Image())
                .product1Price(product.getProduct1Price())
                .product1Link(product.getProduct1Link())

                .brand2(product.getBrand2())
                .product2(product.getProduct2())
                .product2Image(product.getProduct2Image())
                .product2Price(product.getProduct2Price())
                .product2Link(product.getProduct2Link())

                .brand3(product.getBrand3())
                .product3(product.getProduct3())
                .product3Image(product.getProduct3Image())
                .product3Price(product.getProduct3Price())
                .product3Link(product.getProduct3Link())

                .brand4(product.getBrand4())
                .product4(product.getProduct4())
                .product4Image(product.getProduct4Image())
                .product4Price(product.getProduct4Price())
                .product4Link(product.getProduct4Link())

                .brand5(product.getBrand5())
                .product5(product.getProduct5())
                .product5Image(product.getProduct5Image())
                .product5Price(product.getProduct5Price())
                .product5Link(product.getProduct5Link())

                .build();
    }
}
