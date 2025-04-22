package com.example.capstone.skincaremission.controller;

import com.example.capstone.skincaremission.dto.DailyMissionResponse;
import com.example.capstone.skincaremission.dto.MissionCheckRequest;
import com.example.capstone.skincaremission.dto.MissionScoreResponse;
import com.example.capstone.skincaremission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/care-mission")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    // 최신 분석 결과로 미션 세트 생성 (기존 미션이 있다면 초기화)
    @PostMapping("/create")
    public ResponseEntity<?> createMissionSet(Authentication authentication) {
        try {
            missionService.createMissionSet(authentication);
            return ResponseEntity.ok(Map.of("message", "미션이 성공적으로 생성되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }

    // 오늘의 미션 + 미션 수행 여부 조회
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyMissions(Authentication authentication) {
        try {
            List<DailyMissionResponse> response = missionService.getTodayMission(authentication);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 미션 체크 저장
    @PostMapping("/daily/check")
    public ResponseEntity<?> saveMissionCheck(
            @RequestBody MissionCheckRequest request,
            Authentication authentication
    ) {
        try {
            missionService.saveTodayCheck(authentication, request);
            return ResponseEntity.ok(Map.of("message", "미션 체크 저장이 완료되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 미션 점수 계산
    @GetMapping("/score")
    public ResponseEntity<?> getAverageScore(Authentication authentication) {
        MissionScoreResponse response = missionService.calculateAverageScore(authentication);
        return ResponseEntity.ok(response);
    }
}
