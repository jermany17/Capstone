package com.example.capstone.aibeauty.repository;

import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SkinAnalysisResultRepository extends JpaRepository<SkinAnalysisResult, String> {

    // 분석 id로 조회 (검사 결과 조회용)
    Optional<SkinAnalysisResult> findByAnalysisId(String analysisId);

    // 지정한 사용자 id에 해당하는 피부 분석 결과 조회
    List<SkinAnalysisResult> findAllByUserId(String userId);

    // 지정한 사용자 id와 지정한 날짜에 해당하는 피부 분석 결과 조회
    Optional<SkinAnalysisResult> findByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
