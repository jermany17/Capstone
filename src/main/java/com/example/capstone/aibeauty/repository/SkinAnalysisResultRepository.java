package com.example.capstone.aibeauty.repository;

import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SkinAnalysisResultRepository extends JpaRepository<SkinAnalysisResult, String> {

    // 분석 id로 조회 (검사 결과 조회용)
    Optional<SkinAnalysisResult> findByAnalysisId(String analysisId);

    // 오늘 날짜에 이미 있는 기존 분석 결과 삭제 (최신 분석 결과만 유지하기 위함)
    void deleteByUserIdAndCreatedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}
