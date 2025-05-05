package com.example.capstone.skincaremission.repository;

import com.example.capstone.skincaremission.domain.UserMissionCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MissionCheckRepository extends JpaRepository<UserMissionCheck, Long> {

    void deleteByUserId(String userId);

    Optional<UserMissionCheck> findByUserIdAndDate(String userId, LocalDate date);

    Optional<UserMissionCheck> findTopByUserIdOrderByDateAsc(String userId);

    // 미션 총합 점수 상위 3명 가져오기 (동점이 있으면 userId 순서에 따라서)
    @Query("SELECT c.userId, SUM(c.doneCount) " +
            "FROM UserMissionCheck c " +
            "GROUP BY c.userId " +
            "ORDER BY SUM(c.doneCount) DESC, c.userId ASC")
    List<Object[]> findTopMissionScorers(Pageable pageable);
}
