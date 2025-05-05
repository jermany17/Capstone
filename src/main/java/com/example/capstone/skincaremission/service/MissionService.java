package com.example.capstone.skincaremission.service;

import com.example.capstone.aibeauty.domain.SkinAnalysisResult;
import com.example.capstone.aibeauty.repository.SkinAnalysisResultRepository;
import com.example.capstone.auth.domain.User;
import com.example.capstone.skincaremission.domain.MissionTemplate;
import com.example.capstone.skincaremission.domain.UserMissionCheck;
import com.example.capstone.skincaremission.domain.UserMissionSet;
import com.example.capstone.skincaremission.dto.*;
import com.example.capstone.skincaremission.repository.MissionCheckRepository;
import com.example.capstone.skincaremission.repository.MissionSetRepository;
import com.example.capstone.skincaremission.repository.MissionTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private final MissionSetRepository missionSetRepository;
    private final MissionCheckRepository missionCheckRepository;
    private final SkinAnalysisResultRepository analysisResultRepository;
    private final MissionTemplateRepository templateRepository;

    // 미션 생성
    public void createMissionSet(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userId = user.getUserId();

        // 기존 미션 세트 + 미션 체크 기록 삭제 (초기화)
        missionSetRepository.deleteByUserId(userId);
        missionCheckRepository.deleteByUserId(userId);

        // 최신 분석 결과 조회
        SkinAnalysisResult result = analysisResultRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("피부 분석 결과가 존재하지 않습니다. 먼저 피부 분석을 진행해주세요."));

        // 미션 템플릿에서 분석 결과에 맞는 미션 가져오기
        List<MissionTemplate> missions = new ArrayList<>();

        String wrinkleScore = result.getTotalWrinkle() <= 13 ? "low" : "high";
        String pigmentationScore = result.getTotalPigmentation() <= 7 ? "low" : "high";
        String poreScore = result.getTotalPore() <= 5 ? "low" : "high";
        String lipScore = result.getLipDryness() <= 2 ? "low" : "high";
        String jawScore = result.getJawlineSagging() <= 3 ? "low" : "high";

        missions.add(templateRepository.findByCategoryAndScore("주름", wrinkleScore)
                .orElseThrow(() -> new RuntimeException("주름 미션이 없습니다.")));
        missions.add(templateRepository.findByCategoryAndScore("색소침착", pigmentationScore)
                .orElseThrow(() -> new RuntimeException("색소침착 미션이 없습니다.")));
        missions.add(templateRepository.findByCategoryAndScore("모공", poreScore)
                .orElseThrow(() -> new RuntimeException("모공 미션이 없습니다.")));
        missions.add(templateRepository.findByCategoryAndScore("입술건조도", lipScore)
                .orElseThrow(() -> new RuntimeException("입술건조도 미션이 없습니다.")));
        missions.add(templateRepository.findByCategoryAndScore("턱선처짐", jawScore)
                .orElseThrow(() -> new RuntimeException("턱선처짐 미션이 없습니다.")));

        UserMissionSet set = UserMissionSet.builder()
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .mission1Id(missions.get(0).getId())
                .mission2Id(missions.get(1).getId())
                .mission3Id(missions.get(2).getId())
                .mission4Id(missions.get(3).getId())
                .mission5Id(missions.get(4).getId())
                .build();

        missionSetRepository.save(set);
    }

    // 오늘 미션 불러오기
    public List<DailyMissionResponse> getTodayMission(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userId = user.getUserId();

        // 미션 세트 조회, 미션 세트가 없다면 예외 처리
        UserMissionSet missionSet = missionSetRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("미션 세트가 없습니다. 먼저 미션을 생성해주세요."));

        // 미션 체크 기록이 한달 이상 누적된 경우 초기화 (기록이 너무 쌓이는 거 방지)
        resetCheckIfAccumulatedOneMonth(userId);

        // 오늘 미션 체크 정보 불러오기 (없으면 null)
        LocalDate today = LocalDate.now();
        UserMissionCheck check = missionCheckRepository.findByUserIdAndDate(userId, today)
                .orElse(null);

        // 미션 내용 불러오기
        List<MissionTemplate> missions = List.of(
                templateRepository.findById(missionSet.getMission1Id())
                        .orElseThrow(() -> new RuntimeException("mission1 템플릿이 존재하지 않습니다.")),
                templateRepository.findById(missionSet.getMission2Id())
                        .orElseThrow(() -> new RuntimeException("mission2 템플릿이 존재하지 않습니다.")),
                templateRepository.findById(missionSet.getMission3Id())
                        .orElseThrow(() -> new RuntimeException("mission3 템플릿이 존재하지 않습니다.")),
                templateRepository.findById(missionSet.getMission4Id())
                        .orElseThrow(() -> new RuntimeException("mission4 템플릿이 존재하지 않습니다.")),
                templateRepository.findById(missionSet.getMission5Id())
                        .orElseThrow(() -> new RuntimeException("mission5 템플릿이 존재하지 않습니다."))
        );

        // 미션, 체크 여부 매핑
        List<DailyMissionResponse> response = new ArrayList<>();
        // 값이 정확히 true일 때만 true, 그 외(false나 null)는 false
        response.add(new DailyMissionResponse(missions.get(0).getMission(), check != null && Boolean.TRUE.equals(check.getMission1Done())));
        response.add(new DailyMissionResponse(missions.get(1).getMission(), check != null && Boolean.TRUE.equals(check.getMission2Done())));
        response.add(new DailyMissionResponse(missions.get(2).getMission(), check != null && Boolean.TRUE.equals(check.getMission3Done())));
        response.add(new DailyMissionResponse(missions.get(3).getMission(), check != null && Boolean.TRUE.equals(check.getMission4Done())));
        response.add(new DailyMissionResponse(missions.get(4).getMission(), check != null && Boolean.TRUE.equals(check.getMission5Done())));

        return response;
    }

    public void saveTodayCheck(Authentication authentication, MissionCheckRequest request) {
        User user = (User) authentication.getPrincipal();
        String userId = user.getUserId();
        LocalDate today = LocalDate.now();

        // 오늘 미션 체크 기록 불러오기, 없으면 오늘 미션 체크 기록 생성
        UserMissionCheck check = missionCheckRepository.findByUserIdAndDate(userId, today)
                .orElseGet(() -> UserMissionCheck.builder()
                        .userId(userId)
                        .date(today)
                        .build());

        // 미션 체크 기록
        check.setMission1Done(request.isMission1());
        check.setMission2Done(request.isMission2());
        check.setMission3Done(request.isMission3());
        check.setMission4Done(request.isMission4());
        check.setMission5Done(request.isMission5());

        // 오늘 수행한 미션 개수 기록
        int count = (int) Stream.of(
                request.isMission1(),
                request.isMission2(),
                request.isMission3(),
                request.isMission4(),
                request.isMission5()
        ).filter(Boolean.TRUE::equals).count();

        check.setDoneCount(count);

        try {
            missionCheckRepository.save(check);
        } catch (Exception e) {
            throw new RuntimeException("미션 체크 저장 중 오류가 발생했습니다.");
        }
    }

    public void resetCheckIfAccumulatedOneMonth(String userId) {
        UserMissionCheck oldest = missionCheckRepository.findTopByUserIdOrderByDateAsc(userId)
                .orElse(null);

        // 제일 오래된 기록으로부터 한달 이상이 지났으면 미션 체크 기록 초기화
        if (oldest != null && LocalDate.now().isAfter(oldest.getDate().plusMonths(1))) {
            try {
                missionCheckRepository.deleteByUserId(userId);
            } catch (Exception e) {
                throw new RuntimeException("미션 체크 기록 초기화 중 오류가 발생했습니다.");
            }
        }
    }

    public MissionScoreResponse calculateAverageScore(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userId = user.getUserId();

        // 미션 시작 날짜 기록 조회
        Optional<UserMissionCheck> firstCheck = missionCheckRepository.findTopByUserIdOrderByDateAsc(userId);

        // 기록이 없는 상태에서 조회 -> 오늘 날짜 기준 0점 반환 (프론트에서 적절한 메시지 출력 예: 아직 미션 시작 안 했습니다.)
        if (firstCheck.isEmpty()) {
            LocalDate today = LocalDate.now();
            return new MissionScoreResponse(today, today, 0, 0);
        }

        // 시작 날짜 ~ 오늘까지 날짜 구간 생성
        LocalDate start = firstCheck.get().getDate();
        LocalDate end = LocalDate.now();
        List<LocalDate> dates = start.datesUntil(end.plusDays(1)).toList(); // 포함 범위

        int totalScore = 0;

        for (LocalDate date : dates) {
            UserMissionCheck check = missionCheckRepository.findByUserIdAndDate(userId, date)
                    .orElse(null);

            // 해당 날짜 기록이 없으면 미션 수행을 안 한 것 = 그럼 0점 처리
            int dailyScore = (check != null && check.getDoneCount() != null) ? check.getDoneCount() : 0;
            totalScore += dailyScore;
        }

        // 미션 점수 계산 (100점 만점, 반올림)
        int averageScore = (int) Math.round((totalScore / (double) (dates.size() * 5)) * 100);

        return new MissionScoreResponse(start, end, averageScore, totalScore);
    }

    // 미션 총합 점수 상위 3명
    public TopMissionScoresResponse getTopMissionScores() {
        Pageable top3 = PageRequest.of(0, 3); // 상위 3명
        List<Object[]> result = missionCheckRepository.findTopMissionScorers(top3);

        List<TopUserScoreDto> topUsers = result.stream()
                .map(row -> new TopUserScoreDto( // TopUserScoreDto 객체로 바꿈
                        (String) row[0],              // userId
                        ((Number) row[1]).intValue()  // doneCount 합
                ))
                .collect(Collectors.toList()); // List로 모음

        return new TopMissionScoresResponse(topUsers); // DTO 리스트 담은 객체로 생성해서 반환
    }
}
