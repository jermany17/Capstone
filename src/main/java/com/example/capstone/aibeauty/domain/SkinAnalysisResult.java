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

    //피부 나이
    @Column(name = "skin_age", nullable = false)
    private int skinAge;

    //분석 결과 11개
    @Column(name = "forehead_wrinkle", nullable = false)
    private int foreheadWrinkle;

    @Column(name = "forehead_pigmentation", nullable = false)
    private int foreheadPigmentation;

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

    @Column(name = "rightcheek_pigmentation", nullable = false)
    private int rightcheekPigmentation;

    @Column(name = "rightcheek_pore", nullable = false)
    private int rightcheekPore;

    @Column(name = "lip_dryness", nullable = false)
    private int lipDryness;

    @Column(name = "jawline_sagging", nullable = false)
    private int jawlineSagging;

    //total 분석 결과 3개
    @Column(name = "total_wrinkle", nullable = false)
    private int totalWrinkle;

    @Column(name = "total_pigmentation", nullable = false)
    private int totalPigmentation;

    @Column(name = "total_pore", nullable = false)
    private int totalPore;

    //피부분석 날짜
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public SkinAnalysisResult(String userId, String analysisId, List<String> imageUrls,
                              int skinAge,
                              int foreheadWrinkle, int foreheadPigmentation,
                              int glabellaWrinkle, int lefteyeWrinkle, int righteyeWrinkle,
                              int leftcheekPigmentation, int leftcheekPore,
                              int rightcheekPigmentation, int rightcheekPore,
                              int lipDryness, int jawlineSagging,
                              int totalWrinkle, int totalPigmentation, int totalPore,
                              LocalDateTime createdAt) {

        this.userId = userId;
        this.analysisId = analysisId;
        this.imageUrls = imageUrls;
        this.skinAge = skinAge;
        this.foreheadWrinkle = foreheadWrinkle;
        this.foreheadPigmentation = foreheadPigmentation;
        this.glabellaWrinkle = glabellaWrinkle;
        this.lefteyeWrinkle = lefteyeWrinkle;
        this.righteyeWrinkle = righteyeWrinkle;
        this.leftcheekPigmentation = leftcheekPigmentation;
        this.leftcheekPore = leftcheekPore;
        this.rightcheekPigmentation = rightcheekPigmentation;
        this.rightcheekPore = rightcheekPore;
        this.lipDryness = lipDryness;
        this.jawlineSagging = jawlineSagging;
        this.totalWrinkle = totalWrinkle;
        this.totalPigmentation = totalPigmentation;
        this.totalPore = totalPore;
        this.createdAt = createdAt;
    }
}
