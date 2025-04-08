package com.example.capstone.aibeauty.domain;

import com.example.capstone.community.domain.StringListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skin_analysis")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkinAnalysisResult {
    // 피부 분석 결과

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    //피부 분석 고유 id
    @Column(name = "analysis_id", nullable = false, unique = true, length = 36)
    private String analysisId;

    @Convert(converter = StringListConverter.class)
    @Column(name = "image_urls", columnDefinition = "TEXT", nullable = false)
    private List<String> imageUrls = new ArrayList<>();

    //분석 결과 19개
    @Column(name = "forehead_wrinkle", nullable = false)
    private int foreheadWrinkle;

    @Column(name = "forehead_pigmentation", nullable = false)
    private int foreheadPigmentation;

    @Column(name = "forehead_moisture", nullable = false)
    private int foreheadMoisture;

    @Column(name = "forehead_elasticity", nullable = false)
    private int foreheadElasticity;

    @Column(name = "glabella_wrinkle", nullable = false)
    private int glabellaWrinkle;

    @Column(name = "lefteye_wrinkle", nullable = false)
    private int lefteyeWrinkle;

    @Column(name = "righteye_wrinkle", nullable = false)
    private int righteyeWrinkle;

    @Column(name = "leftcheek_pigmentation", nullable = false)
    private int leftcheekPigmentation;

    @Column(name = "leftcheek_pore", nullable = false)
    private int leftcheekPore;

    @Column(name = "leftcheek_moisture", nullable = false)
    private int leftcheekMoisture;

    @Column(name = "leftcheek_elasticity", nullable = false)
    private int leftcheekElasticity;

    @Column(name = "rightcheek_pigmentation", nullable = false)
    private int rightcheekPigmentation;

    @Column(name = "rightcheek_pore", nullable = false)
    private int rightcheekPore;

    @Column(name = "rightcheek_moisture", nullable = false)
    private int rightcheekMoisture;

    @Column(name = "rightcheek_elasticity", nullable = false)
    private int rightcheekElasticity;

    @Column(name = "lip_dryness", nullable = false)
    private int lipDryness;

    @Column(name = "jawline_sagging", nullable = false)
    private int jawlineSagging;

    @Column(name = "chin_moisture", nullable = false)
    private int chinMoisture;

    @Column(name = "chin_elasticity", nullable = false)
    private int chinElasticity;

    //total 분석 결과 5개
    @Column(name = "total_wrinkle", nullable = false)
    private int totalWrinkle;

    @Column(name = "total_pigmentation", nullable = false)
    private int totalPigmentation;

    @Column(name = "total_pore", nullable = false)
    private int totalPore;

    @Column(name = "total_moisture", nullable = false)
    private int totalMoisture;

    @Column(name = "total_elasticity", nullable = false)
    private int totalElasticity;

    //피부분석 날짜
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public SkinAnalysisResult(String userId, String analysisId, List<String> imageUrls,
                              int foreheadWrinkle, int foreheadPigmentation, int foreheadMoisture, int foreheadElasticity,
                              int glabellaWrinkle, int lefteyeWrinkle, int righteyeWrinkle,
                              int leftcheekPigmentation, int leftcheekPore, int leftcheekMoisture, int leftcheekElasticity,
                              int rightcheekPigmentation, int rightcheekPore, int rightcheekMoisture, int rightcheekElasticity,
                              int lipDryness, int jawlineSagging, int chinMoisture, int chinElasticity,
                              int totalWrinkle, int totalPigmentation, int totalPore, int totalMoisture, int totalElasticity,
                              LocalDateTime createdAt) {

        this.userId = userId;
        this.analysisId = analysisId;
        this.imageUrls = imageUrls;
        this.foreheadWrinkle = foreheadWrinkle;
        this.foreheadPigmentation = foreheadPigmentation;
        this.foreheadMoisture = foreheadMoisture;
        this.foreheadElasticity = foreheadElasticity;
        this.glabellaWrinkle = glabellaWrinkle;
        this.lefteyeWrinkle = lefteyeWrinkle;
        this.righteyeWrinkle = righteyeWrinkle;
        this.leftcheekPigmentation = leftcheekPigmentation;
        this.leftcheekPore = leftcheekPore;
        this.leftcheekMoisture = leftcheekMoisture;
        this.leftcheekElasticity = leftcheekElasticity;
        this.rightcheekPigmentation = rightcheekPigmentation;
        this.rightcheekPore = rightcheekPore;
        this.rightcheekMoisture = rightcheekMoisture;
        this.rightcheekElasticity = rightcheekElasticity;
        this.lipDryness = lipDryness;
        this.jawlineSagging = jawlineSagging;
        this.chinMoisture = chinMoisture;
        this.chinElasticity = chinElasticity;
        this.totalWrinkle = totalWrinkle;
        this.totalPigmentation = totalPigmentation;
        this.totalPore = totalPore;
        this.totalMoisture = totalMoisture;
        this.totalElasticity = totalElasticity;
        this.createdAt = createdAt;
    }
}
